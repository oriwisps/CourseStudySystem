package com.zjj.coursestudy.controller;

import com.zjj.coursestudy.entity.Discussion;
import com.zjj.coursestudy.entity.KeyWord;
import com.zjj.coursestudy.entity.User;
import com.zjj.coursestudy.model.RespBean;
import com.zjj.coursestudy.service.DiscussionService;
import com.zjj.coursestudy.service.KeyWordService;
import com.zjj.coursestudy.service.UserService;
import com.zjj.coursestudy.utils.JwtUtil;
import com.zjj.coursestudy.vo.DiscussionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/discussion")
public class DiscussionController {

    @Autowired
    private DiscussionService discussionService;

    @Autowired
    private UserService userService;

    @Autowired
    private KeyWordService keyWordService;

    /**
     * 查询用户发送的消息
     * @param token
     * @return
     */
    @GetMapping("/sender")
    public RespBean getDiscussionBySender(@RequestHeader(value = "token") String token){
        RespBean respBean = RespBean.requestError("查询失败");
        String userName = JwtUtil.getUserName(token);

        List<DiscussionVo> discussionVoList = new ArrayList<>();
        User user = userService.getUserByName(userName);
        if(user != null){
            List<Discussion> discussionList = discussionService.findBySender(user);
            for (Discussion d: discussionList) {
                if(d.isSenderVisible()){
                    discussionVoList.add(new DiscussionVo(d));
                }
            }
            respBean = RespBean.ok("查询成功", discussionVoList);
            return respBean;
        }
        return respBean;
    }

    /**
     * 查询用户接收的消息
     * @param token
     * @return
     */
    @GetMapping("/receiver")
    public RespBean getDiscussionByReceiver(@RequestHeader(value = "token") String token){
        RespBean respBean = RespBean.requestError("查询失败");
        String userName = JwtUtil.getUserName(token);

        List<DiscussionVo> discussionVoList = new ArrayList<>();
        User user = userService.getUserByName(userName);
        if(user != null){
            List<Discussion> discussionList = discussionService.findByReceiver(user);
            discussionList.sort(Comparator.comparing(Discussion::getTime).reversed());
            for (Discussion d: discussionList) {
                if(d.isReceiverVisible()){
                    discussionVoList.add(new DiscussionVo(d));
                }
            }
            respBean = RespBean.ok("查询成功", discussionVoList);
            return respBean;
        }
        return respBean;
    }

    /**
     * 发送消息
     * @param token
     * @return
     */
    @PostMapping("/send")
    public RespBean sendDiscussion(@RequestParam(value = "receiverName") String receiverName,
                                   @RequestParam(value = "message") String message,
                                   @RequestParam(value = "replyToID") Integer replyToID,
                                   @RequestHeader(value = "token") String token){
        RespBean respBean = RespBean.requestError("发送失败");
        String userName = JwtUtil.getUserName(token);
        User sender = userService.getUserByName(userName);
        User receiver = userService.getUserByName(receiverName);
        Discussion replyTo = discussionService.getDiscussionByID(replyToID);
        if(sender != null && receiver != null){
            Discussion discussion = new Discussion();
            discussion.setContent(message);
            discussion.setSender(sender);
            discussion.setReceiver(receiver);
            discussion.setTime(new Date(System.currentTimeMillis()));
            discussion.setReplyTo(replyTo);
            discussion.setSenderVisible(true);
            discussion.setReceiverVisible(true);
            discussion = discussionService.saveDiscussion(discussion);
            if(discussion != null){
                respBean = RespBean.ok("发送成功");
                return respBean;
            }
        }
        return respBean;
    }

    /**
     * 发送问题
     * @param keyWordID
     * @param message
     * @param token
     * @return
     */
    @PostMapping("/sendProblem")
    public RespBean sendProblem(@RequestParam(value = "keyWordID") int keyWordID,
                                @RequestParam(value = "message") String message,
                                @RequestHeader(value = "token") String token){
        RespBean respBean = RespBean.requestError("发送失败");

        User student = userService.getUserByName(JwtUtil.getUserName(token));
        KeyWord keyWord = keyWordService.getKeyWordByID(keyWordID);
        if(student != null && keyWord != null){
            Discussion discussion = new Discussion();
            discussion.setReceiver(keyWord.getTeacher());
            discussion.setSender(student);
            discussion.setContent(message);
            discussion.setTime(new Date(System.currentTimeMillis()));
            discussion.setSenderVisible(true);
            discussion.setReceiverVisible(true);
            discussionService.saveDiscussion(discussion);
            respBean = RespBean.ok("发送成功");
        }
        return respBean;
    }

    /**
     * 删除消息
     * @param discussionID
     * @param user
     * @return
     */
    @PostMapping("/deletemsg")
    public RespBean deleteMessage(@RequestParam(value = "discussionID")int discussionID,
                                  @RequestParam(value = "user") String user){
        RespBean respBean = RespBean.requestError("删除失败");
        Discussion discussion = discussionService.getDiscussionByID(discussionID);
        if(discussion != null){
            if(user.equals("sender")){
                discussion.setSenderVisible(false);
            }
            else if(user.equals("receiver")){
                discussion.setReceiverVisible(false);
            }
            discussionService.saveDiscussion(discussion);
            respBean = RespBean.ok("删除成功");
        }
        return respBean;
    }
}
