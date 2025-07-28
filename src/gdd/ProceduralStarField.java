package gdd;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ProceduralStarField {

    // Increased star counts for denser star field
    private List<StarLayer> starLayers;
    private Random random;
    private long seed;

    // Screen dimensions
    private int screenWidth;
    private int screenHeight;

    // Performance optimizations
    private BufferedImage backgroundCache;
    private Graphics2D cacheGraphics;
    private boolean cacheNeedsUpdate = true;
    private int lastUpdateFrame = -1;

    // Threading for updates
    private CompletableFuture<Void> updateTask;

    // Culling optimization
    private Rectangle viewBounds;

    public ProceduralStarField(int screenWidth, int screenHeight, long seed) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.seed = seed;
        this.random = new Random(seed);
        this.viewBounds = new Rectangle(-50, -50, screenWidth + 100, screenHeight + 100);

        // Create background cache
        backgroundCache = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        cacheGraphics = backgroundCache.createGraphics();
        cacheGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF); // Disable AA for cache

        initializeOptimizedStarLayers();
    }

    private void initializeOptimizedStarLayers() {
        starLayers = new ArrayList<>();

        // INCREASED star counts for denser star field
        // Background layer - static, cached - increased from 80 to 300
        starLayers.add(new StarLayer(
                300,           // Increased from 80
                0.1f,          // Very slow
                new Color(80, 80, 100, 120),
                1, 1,          // All same size for batching
                0.0f,          // No twinkling on background layer
                true           // This layer gets cached
        ));

        // Middle layer - increased from 40 to 150
        starLayers.add(new StarLayer(
                150,           // Increased from 40
                0.4f,
                new Color(160, 160, 200, 180),
                2, 2,          // Same size
                0.05f,         // Rare twinkling
                false
        ));

        // Foreground layer - most noticeable - increased from 20 to 80
        starLayers.add(new StarLayer(
                80,            // Increased from 20
                1.0f,
                Color.WHITE,
                3, 4,          // Slight size variation
                0.1f,
                false
        ));

        // NEW: Additional distant layer for even more density
        starLayers.add(new StarLayer(
                200,           // New layer with many small stars
                0.05f,         // Very slow movement
                new Color(60, 60, 80, 80),
                1, 1,          // Small uniform stars
                0.0f,          // No twinkling
                true           // Cached for performance
        ));

        generateInitialStars();
    }

    private void generateInitialStars() {
        for (StarLayer layer : starLayers) {
            generateStarsForLayer(layer);
        }
    }

    private void generateStarsForLayer(StarLayer layer) {
        layer.stars.clear();

        int bufferHeight = screenHeight + 200; // Smaller buffer

        for (int i = 0; i < layer.starCount; i++) {
            Star star = new Star();
            star.x = random.nextInt(screenWidth);
            star.y = random.nextInt(bufferHeight) - 100;
            star.size = layer.minSize + random.nextInt(Math.max(1, layer.maxSize - layer.minSize + 1));
            star.brightness = 0.8f + random.nextFloat() * 0.2f; // Less variation
            star.twinklePhase = random.nextFloat() * 6.28f; // 2*PI
            star.visible = true;
            layer.stars.add(star);
        }
    }

    // Async update method
    public void updateAsync(float scrollSpeed, int frame) {
        // Don't update every frame - skip some for performance
        if (frame - lastUpdateFrame < 2) return; // Update every 2nd frame

        lastUpdateFrame = frame;

        // Cancel previous update if still running
        if (updateTask != null && !updateTask.isDone()) {
            updateTask.cancel(false);
        }

        // Run update in background thread
        updateTask = CompletableFuture.runAsync(() -> {
            updateStarLayers(scrollSpeed, frame);
        });
    }

    // Synchronous update for simpler usage
    public void update(float scrollSpeed, int frame) {
        if (frame - lastUpdateFrame < 1) return; // Limit update frequency
        lastUpdateFrame = frame;
        updateStarLayers(scrollSpeed, frame);
    }

    private void updateStarLayers(float scrollSpeed, int frame) {
        boolean needsCacheUpdate = false;

        for (int i = 0; i < starLayers.size(); i++) {
            StarLayer layer = starLayers.get(i);
            boolean layerChanged = updateLayer(layer, scrollSpeed * layer.scrollSpeedMultiplier, frame);

            if (layer.cached && layerChanged) {
                needsCacheUpdate = true;
            }
        }

        if (needsCacheUpdate) {
            cacheNeedsUpdate = true;
        }
    }

    private boolean updateLayer(StarLayer layer, float effectiveScrollSpeed, int frame) {
        boolean changed = false;

        // Batch process stars
        List<Star> starsToRemove = new ArrayList<>();
        List<Star> starsToAdd = new ArrayList<>();

        for (Star star : layer.stars) {
            // Move star
            star.y += effectiveScrollSpeed;

            // Update visibility (frustum culling)
            star.visible = viewBounds.contains(star.x, star.y);

            // Simple twinkling - only for visible stars
            if (star.visible && layer.twinkleChance > 0 && random.nextFloat() < layer.twinkleChance) {
                star.twinklePhase += 0.1f;
                if (star.twinklePhase > 6.28f) star.twinklePhase = 0;
            }

            // Remove off-screen stars
            if (star.y > screenHeight + 50) {
                starsToRemove.add(star);
                changed = true;
            }
        }

        // Batch remove
        layer.stars.removeAll(starsToRemove);

        // Add new stars to maintain count
        while (layer.stars.size() < layer.starCount) {
            Star newStar = new Star();
            newStar.x = random.nextInt(screenWidth);
            newStar.y = -random.nextInt(20);
            newStar.size = layer.minSize + random.nextInt(Math.max(1, layer.maxSize - layer.minSize + 1));
            newStar.brightness = 0.8f + random.nextFloat() * 0.2f;
            newStar.twinklePhase = random.nextFloat() * 6.28f;
            newStar.visible = true;
            layer.stars.add(newStar);
            changed = true;
        }

        return changed;
    }

    public void draw(Graphics2D g2d) {
        // Draw cached background layers first
        if (cacheNeedsUpdate) {
            updateBackgroundCache();
        }
        g2d.drawImage(backgroundCache, 0, 0, null);

        // Draw dynamic layers with minimal rendering quality
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        // Skip cached layers (background layers) since they're cached
        for (int i = 0; i < starLayers.size(); i++) {
            if (!starLayers.get(i).cached) {
                try {
                    drawLayerOptimized(g2d, starLayers.get(i));
                } catch (Exception e) {}
            }
        }
    }

    private void updateBackgroundCache() {
        // Clear cache
        cacheGraphics.setComposite(AlphaComposite.Clear);
        cacheGraphics.fillRect(0, 0, screenWidth, screenHeight);
        cacheGraphics.setComposite(AlphaComposite.SrcOver);

        // Draw all cached layers to cache
        for (int i = 0; i < starLayers.size(); i++) {
            if (starLayers.get(i).cached) {
                drawLayerOptimized(cacheGraphics, starLayers.get(i));
            }
        }
        cacheNeedsUpdate = false;
    }

    private void drawLayerOptimized(Graphics2D g2d, StarLayer layer) {
        // Batch draw same-sized stars
        Map<Integer, List<Star>> starsBySize = new HashMap<>();

        try {
            for (Star star : layer.stars) {
                if (!star.visible) continue;

                starsBySize.computeIfAbsent(star.size, k -> new ArrayList<>()).add(star);
            }
        } catch (Exception e) {}

        // Draw each size batch
        for (int size : starsBySize.keySet()) {
            drawStarBatch(g2d, starsBySize.get(size), layer, size);
        }
    }

    private void drawStarBatch(Graphics2D g2d, java.util.List<Star> stars, StarLayer layer, int size) {
        for (Star star : stars) {
            // Simplified brightness calculation
            float brightness = star.brightness;
            if (layer.twinkleChance > 0) {
                brightness *= (0.9f + 0.1f * (float)Math.sin(star.twinklePhase));
            }

            // Use pre-calculated colors when possible
            Color starColor = getOptimizedColor(layer.baseColor, brightness);
            g2d.setColor(starColor);

            // Simplified star drawing - no glow effects on smaller stars
            if (size <= 2) {
                g2d.fillRect(star.x, star.y, size, size); // Faster than fillOval
            } else {
                g2d.fillOval(star.x - size/2, star.y - size/2, size, size);

                // Only add sparkle to largest, brightest stars
                if (size >= 4 && brightness > 0.95f) {
                    g2d.setColor(Color.WHITE);
                    g2d.drawLine(star.x - size - 1, star.y, star.x + size + 1, star.y);
                    g2d.drawLine(star.x, star.y - size - 1, star.x, star.y + size + 1);
                }
            }
        }
    }

    // Color cache for performance
    private Map<String, Color> colorCache = new HashMap<>();

    private Color getOptimizedColor(Color baseColor, float brightness) {
        // Round brightness to reduce cache misses
        int brightnessKey = (int)(brightness * 10);
        String key = baseColor.getRGB() + "_" + brightnessKey;

        return colorCache.computeIfAbsent(key, k -> {
            return new Color(
                    Math.min(255, (int)(baseColor.getRed() * brightness)),
                    Math.min(255, (int)(baseColor.getGreen() * brightness)),
                    Math.min(255, (int)(baseColor.getBlue() * brightness)),
                    baseColor.getAlpha()
            );
        });
    }

    // Simplified effects - only occasionally
    public void addRandomEffects(Graphics2D g2d, int frame) {
        // Much less frequent effects
        if (frame % 900 == 0 && random.nextFloat() < 0.2f) { // Every 15 seconds
            drawSimpleShootingStar(g2d);
        }
    }

    private void drawSimpleShootingStar(Graphics2D g2d) {
        int startX = random.nextInt(screenWidth);
        int startY = -5;
        int length = 20;

        g2d.setColor(new Color(255, 255, 200, 100));
        g2d.drawLine(startX, startY, startX + length, startY + length);
    }

    // Cleanup method
    public void dispose() {
        if (cacheGraphics != null) {
            cacheGraphics.dispose();
        }
        if (updateTask != null) {
            updateTask.cancel(true);
        }
    }

    // Inner classes remain mostly the same
    private static class StarLayer {
        List<Star> stars = new ArrayList<>();
        int starCount;
        float scrollSpeedMultiplier;
        Color baseColor;
        int minSize, maxSize;
        float twinkleChance;
        boolean cached; // New field

        StarLayer(int count, float scrollSpeed, Color color, int minSize, int maxSize, float twinkle, boolean cached) {
            this.starCount = count;
            this.scrollSpeedMultiplier = scrollSpeed;
            this.baseColor = color;
            this.minSize = minSize;
            this.maxSize = maxSize;
            this.twinkleChance = twinkle;
            this.cached = cached;
        }
    }

    private static class Star {
        int x, y;
        int size;
        float brightness;
        float twinklePhase;
        boolean visible; // New field for culling
    }
}