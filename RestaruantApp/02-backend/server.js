const express = require("express");
const http = require("http");
const socketIo = require("socket.io");
const cors = require("cors");

const app = express();
const server = http.createServer(app);
const io = socketIo(server, {
  cors: {
    origin: "*", // อนุญาตให้ทุกโดเมนเชื่อมต่อ
    methods: ["GET", "POST"],
  },
});

app.use(cors());
app.use(express.json());

io.on("connection", (socket) => {
  console.log("Client connected:", socket.id);

  socket.on("disconnect", () => {
    console.log("Client disconnected:", socket.id);
  });
});

// API สำหรับแจ้งเตือนออเดอร์ใหม่
app.post("/new-order", (req, res) => {
  const order = req.body;
  console.log("New order received:", order);

  // ส่งแจ้งเตือนไปยังทุก client
  io.emit("newOrder", order);

  res.status(200).json({ message: "Order notification sent" });
});

const PORT = 3000;
server.listen(PORT, () => {
  console.log(`WebSocket Server running on port ${PORT}`);
});
