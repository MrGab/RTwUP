package storm.bolts;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import storage.URLMap;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/**
 * This bolt counts the URL.
 * 
 * @author Gabriele de Capoa, Gabriele Proni, Daniele Morgantini
 * 
 */

public class URLCounterBolt extends BaseBasicBolt {

	private static final long serialVersionUID = 1L;

	private ConcurrentHashMap<String, Map<String, Integer>> counts;

	public void prepare(Map conf, TopologyContext context) {
		this.counts = URLMap.getInstance();
	}

	public void execute(Tuple input, BasicOutputCollector collector) {

		String domain = input.getStringByField("expanded_url_domain");
		String file = input.getStringByField("expanded_url_file");
		Integer count = 1;
		Map<String, Integer> ranking = null;

		ranking = this.counts.get(domain);
		if (ranking == null) {
			ranking = new HashMap<String, Integer>();
		} else {
			count = ranking.get(file);
			if (count == null)
				count = 1;
			else
				count++;
		}
		ranking.put(file, count);
		this.counts.put(domain, ranking);
		
		System.err.println("Domain: " + domain + " File: " + file + " Count: "
				+ count);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}

}
