import 'package:flame/game.dart';
import 'package:flutter/material.dart';

class MyGame extends FlameGame {
  double x = 0;

  @override
  void render(Canvas canvas) {
    super.render(canvas);
    final paint = Paint()..color = const Color(0xFF00FF00);
    canvas.drawRect(Rect.fromLTWH(x, 100, 100, 100), paint);
  }

  @override
  void update(double dt) {
    super.update(dt);
    x += 100 * dt; // เคลื่อนที่ไปทางขวา
    if (x > size.x) {
      x = 0;
    }
  }
}
