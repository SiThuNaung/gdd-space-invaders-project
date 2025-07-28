# 🚀 Space Invaders Java Game

A vertical scrolling shooter game developed as a university project. This game features two progressive stages and a final boss battle, all created using Java and the built-in Swing framework. The game utilizes animated sprites, open-source assets, CSV-driven enemy waves, and dynamic transitions to create an engaging arcade-style experience.

---

## 🎓 Project Information

- **University:** Assumption University, Thailand
- **Course Codes:** ITX4606(542), CSX4515(542)
- **Project Forked From:**  
  Original repository: [mchayapol/gdd-space-invaders-project](https://github.com/mchayapol/gdd-space-invaders-project)  
  Credits to [@mchayapol](https://github.com/mchayapol)
- **Base Reference:**  
  Inspired by the [Java Space Invaders](https://github.com/janbodnar/Java-Space-Invaders) project by Jan Bodnar

---

## 👨‍💻 Team Members / Collaborators

| Name            | Student ID | GitHub Profile                           |
|-----------------|------------|------------------------------------------|
| Min Thant       | 6612012    | [Minn01](https://github.com/Minn01)      |
| Thant Zin Min   | 6611968    | [Tommyzizii](https://github.com/Tommyzizii) |
| Si Thu Naung    | 6530287    | [SiThuNaung](https://github.com/SiThuNaung) |

---

## 🛜 Check out the gameplay video on 
YouTube: [Space Invaders Java Game](https://www.youtube.com/watch?v=example) (replace with actual link)

## 📷 Screenshots
add screenshots here or link to a gallery

## 🕹️ Game Features

- 🔫 **Classic vertical shooter gameplay** with responsive movement
- 🎮 **Two action-packed stages** followed by a **boss stage**
- 🕒 Approx. **10 minutes of gameplay**
- 🧠 **Enemy spawn logic loaded from CSV file**
- 👾 **Three enemy types** + animated **boss**
- 🎞️ **Smooth stage transitions** with animated effects
- 🖼️ **Procedurally generated backgrounds**
- 💾 **High score saving system** using JSON file storage
- 💥 **Player hit reaction system**:
  - When the player gets hit, they receive a **1-second cooldown**
  - During this cooldown, the player's sprite becomes **semi-transparent** to indicate invulnerability
- 🎯 **Power-ups**:
  - **Shield**: Temporary 30-second invincibility; does not stack
  - **Extra Life**: Adds a life to the original 3-life system
  - **Gun Upgrade**: Increases bullet count (up to 4 bullets at once)
  - **Speed Boost**: Enhances movement speed (up to 5 levels)
- ✈️ **Responsive movement** system with vertical, horizontal, and diagonal directions
- 🔊 **Audio Design**:
  - Background music for **title screen**, **main game**, and **boss stage**
  - Overlapping sound effects (explosions, lasers, power-ups) managed with audio clip pools
- 🎨 All assets (sprites, music, sound effects) are **open source**

---
## 🧪 Language Used

- **Programming Language**: Java
- **Graphics Library**: Java Swing (AWT/Swing)
- **Data Storage**: JSON (high scores), CSV (enemy spawn waves)

---

## 💾 How to Run

- Recommended: Java Development Kit (JDK) 8 or higher
- Recommended: to run in an IDE like IntelliJ IDEA or Eclipse 
- The entry point for the game is in gdd package `gdd.Main.java` in the `src` directory.
