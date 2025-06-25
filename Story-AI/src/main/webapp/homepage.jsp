<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>AI Generator</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f0f8f0;
            margin: 0;
            padding: 0;
            color: #333;
        }

        .navbar {
            background-color: #28a745;
            padding: 15px 20px;
            color: white;
            font-size: 20px;
            font-weight: bold;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }

        h1 {
            color: #28a745;
            text-align: center;
            margin-top: 20px;
        }

        form {
            background: #ffffff;
            padding: 20px;
            max-width: 600px;
            margin: 20px auto;
            border-radius: 10px;
            box-shadow: 0 4px 10px rgba(0,0,0,0.1);
        }

        label {
            font-weight: bold;
        }

        textarea {
            width: 100%;
            height: 200px;
            padding: 10px;
            margin-top: 10px;
            border: 2px solid #ccc;
            border-radius: 8px;
            resize: vertical;
            font-size: 16px;
        }

        input[type="submit"] {
            background-color: #28a745;
            color: white;
            border: none;
            padding: 10px 20px;
            font-size: 16px;
            border-radius: 8px;
            cursor: pointer;
            margin-top: 10px;
        }

        input[type="submit"]:hover {
            background-color: #1e7e34;
        }

        .result-box, .error-box {
            max-width: 600px;
            margin: 20px auto;
            padding: 15px;
            border-radius: 8px;
        }

        .result-box {
            background: #e0f7ea;
            color: #1b5e20;
        }

        .error-box {
            background: #ffebee;
            color: #c62828;
        }

        .counter {
            font-size: 14px;
            margin-top: 5px;
            color: #555;
            text-align: right;
        }

        #readMoreBtn {
            margin-top: 15px;
            background-color: #28a745;
            color: white;
            border: none;
            padding: 8px 16px;
            border-radius: 6px;
            cursor: pointer;
            display: block;
            text-align: left;
        }

        #readMoreBtn:hover {
            background-color: #1e7e34;
        }

        .read-more-wrapper {
            text-align: left;
        }
    </style>

    <script>
        function countWords(text) {
            return text.trim().split(/\s+/).filter(word => word !== "").length;
        }

        function checkWordLimit() {
            const textarea = document.getElementById("prompt");
            const wordCount = countWords(textarea.value);

            if (wordCount > 10000) {
                alert("Please enter no more than 10,000 words.");
                return false;
            }
            return true;
        }

        function updateCounter() {
            const textarea = document.getElementById("prompt");
            const counter = document.getElementById("wordCounter");
            const wordCount = countWords(textarea.value);

            counter.textContent = `Words: ${wordCount}/10000`;
            counter.style.color = wordCount > 10000 ? "red" : "#555";
        }

        function showMore() {
            document.getElementById("extraContent").style.display = "block";
            document.getElementById("readMoreBtn").style.display = "none";
        }

        window.onload = () => {
            const prompt = document.getElementById("prompt");
            if (prompt) {
                prompt.addEventListener("input", updateCounter);
            }
        };
    </script>
</head>
<body>

<div class="navbar">sTory Ai</div>

<h1>Welcome to sTory Ai</h1>

<form action="GenerateServlet" method="post" onsubmit="return checkWordLimit()">
    <label for="prompt">Enter Your Prompt (Max 10,000 words):</label><br>
    <textarea id="prompt" name="prompt" required></textarea>
    <div class="counter" id="wordCounter">Words: 0/10000</div>
    <br>
    <input type="submit" value="Generate">
</form>

<!-- ✅ Result Box -->
<c:if test="${not empty result}">
    <div class="result-box">
        <h2>Generated Content:</h2>
        <p id="mainContent">${result}</p>

        <c:if test="${not empty extraInfo}">
            <div class="read-more-wrapper">
                <button id="readMoreBtn" onclick="showMore()">Read More</button>
            </div>
            <div id="extraContent" style="display: none;">
                <hr>
                <p>${extraInfo}</p>
            </div>
        </c:if>
    </div>
</c:if>

<!-- ✅ Error Box -->
<c:if test="${not empty error}">
    <div class="error-box">
        <p>${error}</p>
    </div>
</c:if>

</body>
</html>



