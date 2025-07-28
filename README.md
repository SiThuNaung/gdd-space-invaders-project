# 🚀 Space Invaders Java Game

A vertical scrolling shooter game developed as a university project. This game features two progressive stages and a final boss battle, all created using Java and the built-in Swing framework. The game utilizes animated sprites, open-source assets, CSV-driven enemy waves, and dynamic transitions to create an engaging arcade-style experience.

---

## 🎓 Project Information

- **University:** Assumption University, Thailand
- **Course Codes:** ITX4606(541/542), CSX4515(541/542)
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

<img width="786" height="743" alt="Screenshot 2568-07-28 at 22 04 08" src="https://github.com/user-attachments/assets/1a574c82-fcbc-4a20-9f09-969dcb7b8b06" />
<img width="786" height="743" alt="Screenshot 2568-07-28 at 22 05 40" src="https://github.com/user-attachments/assets/4c6ac892-59ef-4202-98ca-f278b7050859" />
<img width="786" height="743" alt="Screenshot 2568-07-28 at 21 59 05" src="https://github.com/user-attachments/assets/abd1c4bb-019d-4d1d-81b5-82b94232c17e" />
<img width="786" height="743" alt="Screenshot 2568-07-28 at 22 07 43" src="https://github.com/user-attachments/assets/f6fa18fd-3786-444a-8852-1dc4cf3696b6" />
<img width="786" height="743" alt="Screenshot 2568-07-28 at 21 59 26" src="https://github.com/user-attachments/assets/fa053ec7-f350-4989-a2cf-71cb78792694" />
<img width="786" height="743" alt="Screenshot 2568-07-28 at 22 06 19" src="https://github.com/user-attachments/assets/e2daed10-8529-4a22-9a29-587b6252daff" />
<img width="786" height="743" alt="Screenshot 2568-07-28 at 22 00 19" src="https://github.com/user-attachmen
<img width="786" height="743" alt="Screenshot 2568-07-28 at 22 01 12" src="https://github.com/user-attachments/assets/612db1db-27e1-47ff-9d4f-4895ff842afd" />
ts/assets/586fc5df-df39-4123-9e8b-a9c5cd78d1aa" />
<img width="786" height="743" alt="Screenshot 2568-07-28 at 22 01 19" src="https://github.com/user-attachments/assets/ab62a6f9-a689-41da-a61c-577bca6038b9" />
<img width="786" height="743" alt="Screenshot 2568-07-28 at 22 01 22" src="https://github.com/user-attachments/assets/b4643879-5d2e-405b-ac9f-6f99cc3650d5" />
<img width="786" height="743" alt="Screenshot 2568-07-28 at 22 10 22" src="https://github.com/user-attachments/assets/054f51c4-4cc9-41bc-a3a2-aa96df957aff" />
<img width="786" height="743" alt="Screenshot 2568-07-28 at 22 02 06" src="https://github.com/user-attachments/assets/fd810e91-dc66-41a1-bb06-60976ab2a85e" />
<img width="786" height="743" alt="Screenshot 2568-07-28 at 22 02 30" src="https://github.com/user-attachments/assets/7a5da7ff-13a4-4ff5-9415-6d7fbf56c880" />




## 🕹️ Game Features

- 🔫 **Classic vertical shooter gameplay** with responsive movement
- 🎮 **Two action-packed stages** followed by a **boss stage**
- 🕒 Approx. **10 minutes of gameplay**
- 🧠 **Enemy spawn logic loaded from CSV file**
- 👾 **Three enemy types** + animated **boss**
- 1 -> Alien UFO Boombs to Player
- 2 -> Flying Alien Flys and Attacks to Player
- 3 -> Boss Enemy Spawn Her Babies to Player
- 4 -> Baby Boss Try and Catch the Player
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
