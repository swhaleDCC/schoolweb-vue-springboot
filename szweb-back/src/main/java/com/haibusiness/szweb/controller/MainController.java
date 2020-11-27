package com.haibusiness.szweb.controller;

import com.haibusiness.szweb.entity.*;
import com.haibusiness.szweb.service.*;
import com.haibusiness.szweb.vo.HomeItem;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Slf4j
@RestController
public class MainController {
    private CommunityService communityService;

    private DynamicService dynamicService;

    private GraduateService graduateService;

    private NoticeService noticeService;

    private MenuService menuService;

    private CarouselService carouselService;
    private EnrollmentService enrollmentService;

    private StoryService storyService;
    private PartyService partyService;
    private CooperationService cooperationService;
    private CultureService cultureService;

    @GetMapping("/public/home")
    public ResponseEntity index() {
        Map<String,String> menuMap=menuService.getMenus();
        List<HomeItem> items=new ArrayList<>();
        List<HomeItem> subItems=new ArrayList<>();

        HomeItem temp=new HomeItem();
        temp.setName(menuMap.get("notice"));
        temp.setUrl("notice");
        temp.setItems(this.getNoticeList());
        subItems.add(temp);

        temp=new HomeItem();
        temp.setName(menuMap.get("dynamic"));
        temp.setUrl("dynamic");
        temp.setItems(this.getDynamicList());
        subItems.add(temp);

        temp=new HomeItem();
        temp.setName(menuMap.get("hot"));
        temp.setType("hot");
        temp.setUrl("community");
        temp.setItems(this.getHotList());
        subItems.add(temp);

        temp = new HomeItem();
        temp.setName(menuMap.get("enrollment"));
        temp.setUrl("enrollment");
        temp.setItems(this.getEnrollmentList());
        subItems.add(temp);

        temp = new HomeItem();
        temp.setName(menuMap.get("cooperation"));
        temp.setUrl("cooperation");
        temp.setItems(this.getCooperationList());
        subItems.add(temp);

        temp = new HomeItem();
        temp.setName(menuMap.get("party"));
        temp.setUrl("party");
        temp.setItems(this.getPartyList());
        subItems.add(temp);

        temp = new HomeItem();
        temp.setName(menuMap.get("culture"));
        temp.setUrl("culture");
        temp.setItems(this.getCultureList());
        subItems.add(temp);

        temp=new HomeItem();
        temp.setItems(subItems);
        items.add(temp);

        temp=new HomeItem();
        temp.setName(menuMap.get("graduate"));
        temp.setUrl("graduate");
        temp.setItems(this.getGraduateList());
        items.add(temp);

        temp=new HomeItem();
        temp.setName(menuMap.get("carousel"));
        temp.setUrl("carousel");
        temp.setItems(this.getCarouselList());
        items.add(temp);

        temp=new HomeItem();
        temp.setName(menuMap.get("story"));
        temp.setUrl("story");
        temp.setItems(this.getStoryList());
        items.add(temp);

        return ResponseEntity.ok().body(items);
    }

    private List<Community> getHotList() {
        Pageable pageable = PageRequest.of(0,10);
        Page<Community> page = communityService.listCommunitysByTypeAndTitleLike("hot", "", pageable);
        List<Community> list = page.getContent();    // 当前所在页面数据列表
        return list;
    }
    private List<Dynamic> getDynamicList() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Dynamic> page = dynamicService.listDynamicsByTitleLike("", pageable);
        List<Dynamic> list = page.getContent();    // 当前所在页面数据列表

        return list;
    }
    private List<Notice> getNoticeList() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Notice> page = noticeService.listNoticesByTitleLike("", pageable);
        List<Notice> list = page.getContent();    // 当前所在页面数据列表

        return list;
    }
    private List<Graduate> getGraduateList() {
        Pageable pageable = PageRequest.of(0, 3);
        Page<Graduate> page = graduateService.listGraduatesByTitleLike("", pageable);
        List<Graduate> list = page.getContent();    // 当前所在页面数据列表

        return list;
    }
    private List<Enrollment> getEnrollmentList() {
        Pageable pageable = PageRequest.of(0, 3);
        Page<Enrollment> page = enrollmentService.listEnrollmentsByTitleLike("", pageable);
        List<Enrollment> list = page.getContent();    // 当前所在页面数据列表
        
        return list;
    }
    private List<Carousel> getCarouselList() {
        Pageable pageable = PageRequest.of(0, 4);
        Page<Carousel> page =carouselService.listCarouselsByTitleLike("", pageable);
        List<Carousel> list = page.getContent();    // 当前所在页面数据列表

        return list;
    }
    private List<Cooperation> getCooperationList() {
        Pageable pageable = PageRequest.of(0, 4);
        Page<Cooperation> page =cooperationService.listCooperationsByTitleLike("", pageable);
        List<Cooperation> list = page.getContent();    // 当前所在页面数据列表
        return list;
    }
    private List<Story> getStoryList() {
        Pageable pageable = PageRequest.of(0, 4);
        Page<Story> page =storyService.listStorysByTitleLike("", pageable);
        List<Story> list = page.getContent();    // 当前所在页面数据列表
        return list;
    }
    private List<Party> getPartyList() {
        Pageable pageable = PageRequest.of(0, 4);
        Page<Party> page =partyService.listPartysByTitleLike("", pageable);
        List<Party> list = page.getContent();    // 当前所在页面数据列表
        return list;
    }
    private List<Culture> getCultureList() {
        Pageable pageable = PageRequest.of(0, 4);
        Page<Culture> page =cultureService.listCulturesByTitleLike("", pageable);
        List<Culture> list = page.getContent();    // 当前所在页面数据列表
        return list;
    }
}
