package wwf_solver;

import java.io.IOException;
import javax.servlet.http.*;
import java.util.concurrent.ConcurrentSkipListSet;
import org.json.JSONException;
import wwf_solver.DictWrapper;
import wwf_solver.Dict;
import wwf_solver.SolveResult;
import java.util.PriorityQueue;

@SuppressWarnings("serial")
public class WWFSolverServlet extends HttpServlet
{	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{	
		String rack = req.getParameter("rack");
		String start = req.getParameter("start");
		String contains = req.getParameter("contains");
		String end = req.getParameter("end");
		
		if (rack != null && !rack.isEmpty() && rack.length() <= 13 && DictWrapper.count(rack, '*') < 3)
		{
			try
			{
				resp.getWriter().print(getResults(rack,start,contains,end));
			}
			catch (JSONException e)
			{
				resp.getWriter().print("{\"error\":\"" + e.getMessage() + "\"}");
			}
		}
		else if(rack != null && !rack.isEmpty())
		{
			resp.getWriter().print("{\"error\":\"invalid query\"}");
		}
		else
		{
			resp.getWriter().print("{\"error\":\"no query\"}");
		}
	}
	
	public String getResults(String rack, String start, String contains, String end) throws JSONException
	{		
		ConcurrentSkipListSet<SolveResult> results = new ConcurrentSkipListSet<SolveResult>();
		
		Dict dict = DictWrapper.get();
		
		dict.solve(rack, start, contains, end, results);
		
		PriorityQueue<SolveResult> sortedResults = new PriorityQueue<SolveResult>(results);
		
		SolveResult r = sortedResults.poll();
		
		StringBuilder resultText = new StringBuilder("{\"word\":\"" + rack + "\",\"error\":" + false + ",\"results\":[");
		
		while( r != null )
		{
			resultText.append(r.toString());
			r = sortedResults.poll();
			if(r != null) resultText.append(",");
		}
		
		resultText.append("]}");
		
		return resultText.toString();
	}
	
}
