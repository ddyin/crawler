package com.douban.crawltest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 抓取的工具类，获取被抓取数据特征
 * url：https://book.douban.com/tag/编程?start=0&type=T
 * @author ddyin
 *
 */
public class WebSpiderUtil {

	private String url;// 抓取的url.
	private List<DataModel> dataModelList = new ArrayList<DataModel>();// 存放被抓取数据.
	private Set<String> urlSet = new HashSet<String>();// 存放内部url数据.
	private Connection connection = null;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<DataModel> getDataModelList() {
		return dataModelList;
	}

	public void setDataModelList(List<DataModel> dataModelList) {
		this.dataModelList = dataModelList;
	}

	public Set<String> getUrlSet() {
		return urlSet;
	}

	public void setUrlSet(Set<String> urlSet) {
		this.urlSet = urlSet;
	}

	/**
	 * 解析url对应的html，再进一步解析html 递归解析
	 */
	int id = 1;
	int s = 0;

	public void caputerData() {

		try {
			if (url != null && !url.equals("")) {
				System.out.println("正在抓取的页面是：" + url);

				connection = Jsoup.connect(url);
				Document document = connection.timeout(100000).get();

				// 抓取当前页面的内部url数据.
				Elements resultUrls = document.getElementsByClass("paginator");
				Elements links = null;
				for (Element resultUrl : resultUrls) {
					links = resultUrl.getElementsByTag("a");
				}

				// 解析当前url对应的html数据.
				Elements resultDatas = document
						.getElementsByClass("subject-list");
				for (Element resultData : resultDatas) {
					Elements uls = resultData.getElementsByTag("ul");

					for (Element ul : uls) {
						Elements lis = ul.getElementsByTag("li");
						for (int i = 1; lis != null && i < lis.size(); i++) {
							Element li = lis.get(i);
							Elements infos = li.getElementsByClass("info");

							Elements a = infos.get(0).getElementsByTag("a");
							Elements starClearfix = infos.get(3 - 1 - 1 - 1)
									.getElementsByClass("star clearfix");
							Element score = starClearfix.get(0)
									.getElementsByClass("rating_nums").get(0);
							Element commentNumber = starClearfix
									.get(3 - 1 - 1 - 1)
									.getElementsByClass("pl").get(0);

							String divs = infos.get(0)
									.getElementsByClass("pub").text();
							String[] splits = divs.split("/");
							// System.out.println(splits);

							DataModel dataModel = new DataModel();
							dataModel.setId(id);
							dataModel.setBookName(a.get(0).text());
							dataModel.setScore(score.text());
							dataModel.setCommentNumber(commentNumber.text());

							dataModel.setAuthor(splits[0]);
							dataModel.setPublishingHouse(splits[2]);
							dataModel.setPublishingDate(splits[3]);
							dataModel.setPrice(splits[splits.length - 1]);
							dataModelList.add(dataModel);
							id++;
							if (id > 40) {
								return;
							}

						}
					}
				}

				// 难点:获取需要抓取的下一个url,通过Set集合保存已经解析过的url,防止重复解析.
				// urlSet.add(url);
				// boolean flag = false;
				// for(int i = 0; links != null && i < links.size(); i++) {
				//
				// String link = links.get(0).attr("href");
				// if(!urlSet.contains(link)){
				// url="https://book.douban.com"+link;
				// flag=true;
				// break;
				// }
				// }
				boolean flag = false;
				s += 20;

				url = "https://book.douban.com/tag/编程?start=" + s + "&type=T";
				flag = true;

				if (flag) {
					caputerData();
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("抓取数据异常，异常:" + e + "url:" + url);
		}

	}

}
