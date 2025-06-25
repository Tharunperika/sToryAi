package story;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/GenerateServlet")
public class GenerateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public GenerateServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String prompt = request.getParameter("prompt");
        String apiUrl = "https://chatgpt-ai-assistant.p.rapidapi.com/";
        String apiKey = "d245fb7d0fmsh206beae5ec92d42p1a8a7ejsnbb199acf1b7c"; // ✅ Keep it secure in production

        String storyResult = "";
        String extraInfo = "";

        try {
            // 1. Generate Main Story
            storyResult = getChatGPTResponse(prompt, apiUrl, apiKey);

            // 2. Generate Extra Info
            String relatedPrompt = "Give additional or related background information about: " + prompt;
            extraInfo = getChatGPTResponse(relatedPrompt, apiUrl, apiKey);

            // Replace \n with <br> for HTML display
            storyResult = storyResult.replaceAll("\n", "<br>");
            extraInfo = extraInfo.replaceAll("\n", "<br>");

            request.setAttribute("result", storyResult);
            request.setAttribute("extraInfo", extraInfo);
            request.setAttribute("prompt", prompt);

        } catch (Exception e) {
            request.setAttribute("error", "Error: " + e.getMessage());
        }

        request.getRequestDispatcher("homepage.jsp").forward(request, response);
    }

    private String getChatGPTResponse(String prompt, String apiUrl, String apiKey) throws Exception {
        String jsonInputString = "{\n" +
                "  \"model\": \"gpt-3.5-turbo\",\n" +
                "  \"messages\": [\n" +
                "    {\n" +
                "      \"role\": \"user\",\n" +
                "      \"content\": \"" + prompt + "\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        URL url = new URL(apiUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("x-rapidapi-key", apiKey);
        con.setRequestProperty("x-rapidapi-host", "chatgpt-ai-assistant.p.rapidapi.com");
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        StringBuilder responseText = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                responseText.append(line.trim());
            }
        }

        JSONObject jsonResponse = new JSONObject(responseText.toString());
        JSONArray choices = jsonResponse.getJSONArray("choices");
        JSONObject message = choices.getJSONObject(0).getJSONObject("message");

        return message.getString("content");
    }
}
