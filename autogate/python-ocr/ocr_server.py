from flask import Flask, request, jsonify, render_template_string
import easyocr
import cv2
import numpy as np
import requests

app = Flask(__name__)
reader = easyocr.Reader(['th', 'en'])

# ✅ HTML หน้าอัปโหลด
HTML_PAGE = """
<!DOCTYPE html>
<html>
<head>
    <title>OCR Upload</title>
</head>
<body style="font-family:sans-serif; padding:20px;">
    <h2>📸 Upload License Plate Image</h2>
    <form method="post" action="/read-plate" enctype="multipart/form-data">
        <input type="file" name="image" accept="image/*" required />
        <button type="submit">Read Plate</button>
    </form>
</body>
</html>
"""

# ✅ หน้านี้จะเปิดได้ที่ http://localhost:5000/
@app.route('/')
def home():
    return render_template_string(HTML_PAGE)

@app.route('/read-plate', methods=['POST'])
def read_plate():
    file = request.files['image']
    img_bytes = file.read()
    nparr = np.frombuffer(img_bytes, np.uint8)
    img = cv2.imdecode(nparr, cv2.IMREAD_COLOR)

    result = reader.readtext(img)
    plate = ''
    for (bbox, text, conf) in result:
        if 4 <= len(text) <= 10:
            plate = text.replace(" ", "")
            break

    # ส่งผลไป Spring Boot
    response = requests.post(
        "http://localhost:8080/api/gate/check",
        json={"plateNumber": plate}
    )

    # แสดงผลบนหน้าเว็บ
    return f"""
        <h3>Result</h3>
        <p><strong>Plate:</strong> {plate}</p>
        <p><strong>Java Response:</strong> {response.json()}</p>
        <a href="/">⬅️ Back</a>
    """
