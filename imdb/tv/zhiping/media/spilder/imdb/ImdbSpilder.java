package tv.zhiping.media.spilder.imdb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import tv.zhiping.common.util.ComUtil;
import tv.zhiping.media.model.ImdbEpisode;
import tv.zhiping.media.model.ImdbEpisode.ImdbEpisodeType;
import tv.zhiping.utils.FileUtil;
import tv.zhiping.utils.ValidateUtil;


public class ImdbSpilder {
	private Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * 通过电视剧的系列id获取所有的剧集信息
	 * @param root_program_id 电视剧parent_title
	 * @return
	 * @throws IOException
	 */
	public List<ImdbEpisode> getAllEpisodeBySearizeId(String root_program_id) throws IOException{
		String _HOST= "http://www.imdb.com";
		String _SRC_URL = _HOST+"#series_program_id#episodes/_ajax?season=#season#";
		String url = _SRC_URL.replace("#series_program_id#",root_program_id).replace("#season#","1");
		
		Document doc = getHtmlDoc(url);
		List<Integer> seasonList = getSeasonList(doc);
		List<ImdbEpisode> episodeAllList = new ArrayList<ImdbEpisode>();
		episodeAllList.addAll(getEpisodeList(root_program_id,_HOST,doc));
		
		if(seasonList!=null && seasonList.size()>1){//第一页已经遍历了
			for(int i=1;i<seasonList.size();i++){
				Integer season = seasonList.get(i);
				url = _SRC_URL.replace("#series_program_id#",root_program_id).replace("#season#",String.valueOf(season));
				doc = getHtmlDoc(url);
				episodeAllList.addAll(getEpisodeList(root_program_id,_HOST,doc));			
			}
		}
		return episodeAllList;
	}
	
	/**
	 * 根据url内容获取html内容的document
	 * @param url
	 * @return
	 * @throws IOException
	 */
	private Document getHtmlDoc(String url) throws IOException {
		Document doc = Jsoup.connect(url).timeout(10000).get();
		return doc;
	}
	
	//解析获取所有的季
	private List<Integer> getSeasonList(Document doc) {
		List<Integer> seasonList = new ArrayList<Integer>();
		Element seasonHtmlList = doc.getElementById("bySeason");
		if(seasonHtmlList!=null && !seasonHtmlList.isBlock()){
			Elements elements = seasonHtmlList.getAllElements();
			if(elements!=null && !elements.isEmpty()){
				for(int i=0;i<elements.size();i++){
					Element element = elements.get(i);
					if(ValidateUtil.isNum(element.text())){				
						seasonList.add(new Integer(element.text()));
					}
				}			
			}
		}
		return seasonList;
	}
	
	//解析获取所有的节目信息
	private List<ImdbEpisode> getEpisodeList(String pid,String host,Document doc) {
		Elements elements = doc.getElementsByClass("list_item");
		
		List<ImdbEpisode> episodeList = new ArrayList<ImdbEpisode>();
		
		for(int i=0;i<elements.size();i++){
			Element element = elements.get(i);
			
			Elements clsImageElements = element.getElementsByClass("image");
			Elements aElement = clsImageElements.get(0).getElementsByTag("a");
			String href = aElement.attr("href");
			String id = ComUtil.getHttpReqPath(href);
			String url = host+id;
			String title = aElement.attr("title");
			String[] seasonEpisodeStr = aElement.text().split(",");
			String season = seasonEpisodeStr[0].trim().substring(1);
			String current_episode = seasonEpisodeStr[1].trim().substring(2);
			String time = element.getElementsByClass("airdate").text();
			String year = null;
			if(StringUtils.isNotBlank(time)){
				year = time.substring(time.length()-4);
			}
			String summary = element.getElementsByClass("item_description").text();
			
			String images = clsImageElements.get(0).getElementsByTag("img").attr("src");
			images = getImdbBigImg(images);
			
			ImdbEpisode obj = new ImdbEpisode();
			obj.setId(id);
			obj.setPid(pid);
			obj.setUrl(url);
			obj.setTitle(title);
			obj.setOrig_title(title);
			obj.setSeason(season);
			obj.setType(ImdbEpisodeType.tvEpisode.toString());
			obj.setCurrent_episode(new Long(current_episode));
			obj.setYear(year);
			obj.setImages(images);
			obj.setSummary(summary);
			
			episodeList.add(obj);
		}
		return episodeList;
	}



	private String getImdbBigImg(String images) {
		if(images.indexOf("._V1_")<0){
			log.warn("无图="+images);
			images = null;
		}else{
//			images = "http://ia.media-imdb.com/images/M/MV5BMTU0MjEyMTA1MF5BMl5BanBnXkFtZTcwNDg5NjIzMQ@._V1_SY180_CR3,0,120,180_AL_.jpg";
			String suffix = FileUtil.getFileSuffix(images);
			int index = images.lastIndexOf("@");
			if(index>-1){
				images = images.substring(0,index)+"@._V1_."+suffix;
			}
		}
		return images;
	}	
}
