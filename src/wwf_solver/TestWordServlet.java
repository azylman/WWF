package wwf_solver;

import java.io.IOException;
import javax.servlet.http.*;
import org.json.JSONObject;
import org.json.JSONException;
import wwf_solver.DictWrapper;
import wwf_solver.Dict;

@SuppressWarnings("serial")
public class TestWordServlet extends HttpServlet
{
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		String word = req.getParameter("word");
		
		Dict dict = DictWrapper.get();
		
		JSONObject out = new JSONObject();
		
		if (word != null && !word.isEmpty())
		{	
			try {
				out.put("value",DictWrapper.score( dict.isWord(word) ? word : "" ));
				resp.getWriter().print(out.toString());
			}
			catch(JSONException e)
			{
				//we should never get here
			}
		}
		else
		{
			resp.getWriter().print("");
		}
	}
}
