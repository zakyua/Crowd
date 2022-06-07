package com.atguigu.crowd.handler;

import com.atguigu.crowd.api.MySQLRemoteService;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.vo.AddressVO;
import com.atguigu.crowd.entity.vo.LoginMemberVO;
import com.atguigu.crowd.entity.vo.OrderProjectVO;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author ChenCheng
 * @create 2022-06-07 16:29
 */
public class OrderHandler {

    @Autowired
    private MySQLRemoteService mySQLRemoteService;


    /**
     *  去确定回报的内容
     *   根据指定id查询出回报信息
     * @param returnId   回报信息的id
     * @param session
     * @return
     */
    @RequestMapping("/confirm/return/info/{returnId}")
    public String confirmReturnInfo(@PathVariable("returnId") Integer returnId, HttpSession session){


        // 1.取数据库中查询确认回报的内容
        ResultEntity<OrderProjectVO> resultEntity = mySQLRemoteService.getOrderProjectVO(returnId);

        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())){
            // 为了能够在后续操作中保持 orderProjectVO 数据，存入 Session 域
            session.setAttribute("orderProjectVO", resultEntity.getData());
        }


        return "order-confirm-return";

    }


    /**
     *   去确认订单的页面
     * @param returnCount
     * @param session
     * @return
     */
    @RequestMapping("/confirm/order/{returnCount}")
    public String toConfirmOrderPage(
            @PathVariable("returnCount") Integer returnCount,
            HttpSession session ) {

        // 1.先从session中取出orderProjectVO
        OrderProjectVO orderProjectVO = (OrderProjectVO)session.getAttribute("orderProjectVO");
        // 2.给orderProjectVO设置回报数量
        orderProjectVO.setReturnCount(returnCount);
        // 3.orderProjectVO重新放回到session域中
        session.setAttribute("orderProjectVO",orderProjectVO);
        // 4.获取当前已登录用户的 id
        LoginMemberVO loginMember = (LoginMemberVO)session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        Integer memberId = loginMember.getId();

        // 5.查询当前用户的默认地址
        ResultEntity<List<AddressVO>> resultEntity = mySQLRemoteService.getAddressListByMemberIdRemote(memberId);


        // 6.如果查询到，说明当前用户有默认地址
        if(ResultEntity.SUCCESS.equals(resultEntity.getResult())){
            // 7.则将收货地址放入session域
            List<AddressVO> addressVOList = resultEntity.getData();
            session.setAttribute("addressVOList", addressVOList);

        }

        // 返回确认订单的页面
        return "order-confirm-order";
    }


    /**
     *
     * @param addressVO
     * @param session
     * @return
     */
    @RequestMapping("/save/address")
    public String saveAddress(AddressVO addressVO, HttpSession session){

        // 1.调用远程的方法保存地址
       ResultEntity<String> resultEntity = mySQLRemoteService.saveAddressRemote(addressVO);

        // 2.从session中取出orderProjectVO
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute("orderProjectVO");
        Integer returnCount = orderProjectVO.getReturnCount();

        // 再次重定向到确认订单的页面（附带回报数量）
        return "redirect:http://localhost/order/confirm/order/" + returnCount;
    }

}
