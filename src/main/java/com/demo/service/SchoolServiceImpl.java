package com.demo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.demo.dto.SchoolDTO;
import com.demo.util.MongDbUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created with IntelliJ IDEA
 *
 * @author: yangzhizhuang
 * @date: 2019/12/7
 * @description:
 */
@Service
public class SchoolServiceImpl {

    @Autowired
    private MongDbUtil mongDbUtil;

    public void getSchools(){
        int page = totalPage(2834,20);

        for(int i = 1 ; i <= page ; i++){
            RestTemplate restTemplate = new RestTemplate();
            SchoolDTO dto = new SchoolDTO(1,i,20);
            String str = restTemplate.postForObject("https://api.eol.cn/gkcx/api/?access_token=&admissions=&central=&department=&dual_class=&f211=&f985=&is_dual_class=&keyword=&page=1&province_id=&request_type=1&school_type=&signsafe=&size=20&sort=view_total&type=&uri=apigkcx/api/school/hotlists",dto, String.class);
            JSONObject json = JSON.parseObject(str);
            JSONArray arr = json.getJSONObject("data").getJSONArray("item");
            for(int j = 0 ; j < arr.size() ; j++){
                JSONObject rs = arr.getJSONObject(j);
                mongDbUtil.insert("School",rs);
            }

        }

//        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
//        postParameters.add("request_type", 1);
//        postParameters.add("page",(i+1));
//        postParameters.add("size",20);
//        postParameters.add("startDate", DateUtils.format(entity.getLessonBeginTime(),"yyyy-MM-dd HH:mm:ss"));
//        postParameters.add("subject",entity.getLessonName());
//
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/x-www-form-urlencoded");
//        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(postParameters, headers);




    }

    /**
     * 根据总数计算总页数
     *
     * @param totalCount 总数
     * @param pageSize 每页数
     * @return 总页数
     */
    public static int totalPage(int totalCount, int pageSize) {
        if (pageSize == 0) {
            return 0;
        }
        return totalCount % pageSize == 0 ? (totalCount / pageSize) : (totalCount / pageSize + 1);
    }
}
