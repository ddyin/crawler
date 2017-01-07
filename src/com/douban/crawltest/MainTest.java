package com.douban.crawltest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainTest {

	
	public static void main(String[] args) {
		
		//抓取数据.  
        WebSpiderUtil wsu = new WebSpiderUtil();  
        wsu.setUrl("https://book.douban.com/tag/编程?start=0&type=T"); 
        
        long start = System.currentTimeMillis(); 
        
        wsu.caputerData();
        System.out.println("抓取数据共耗时:" + (System.currentTimeMillis() - start)/1000 + "秒");  
        
        
      //过滤重复数据.  
        List<DataModel> all = wsu.getDataModelList();  
        List<DataModel> del = new ArrayList<DataModel>();  
        List<DataModel> save = new ArrayList<DataModel>();  
        System.out.println("抓取到的数据共:" + all.size());  
        for(int i = 0; all != null && i < all.size(); i++) {  
            boolean flag = false;  
            DataModel dm = all.get(i);  
            for(int j = 0; save != null && j < save.size(); j++) {  
                DataModel dm2 = save.get(j);  
                if(dm.getBookName().equals(dm2.getBookName())) {  
                    del.add(dm2);  
                    flag = true;  
                    break;  
                }   
            }  
            if(flag == false) {  
                save.add(dm);  
            }  
        }  
        
        //将过滤后数据写入到excel中.
        JxlUtil ju = new JxlUtil();  
        ju.setPath("E:\\crawler\\model.xls");  
        Map<String, List<List<String>>> listListMap = new HashMap<String, List<List<String>>>();  
        List<List<String>> listList = new ArrayList<List<String>>();  
        List<String> list1 = new ArrayList<String>();  
        list1.add("序号");  
        list1.add("书名");  
        list1.add("评分");  
        list1.add("评价人数");  
        list1.add("作者");  
        list1.add("出版社");  
        list1.add("出版日期");  
        list1.add("价格");  
        listList.add(list1);  
        for(DataModel dm : save) {  
            List<String> list = new ArrayList<String>();  
            list.add(dm.getId()+"");  
            list.add(dm.getBookName());  
            list.add(dm.getScore());  
            list.add(dm.getCommentNumber());  
            list.add(dm.getAuthor());  
            list.add(dm.getPublishingHouse());  
            list.add(dm.getPublishingDate());  
            list.add(dm.getPrice());  
            listList.add(list);  
        }  
        System.out.println("存入到excel的数据共:" + save.size());  
        listListMap.put("编程书籍", listList);  
        ju.write(listListMap);  
        
        
	}
	
	
}
