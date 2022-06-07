package com.atguigu.crowd.Service.impl;

import com.atguigu.crowd.Service.api.OrderService;
import com.atguigu.crowd.entity.po.AddressPO;
import com.atguigu.crowd.entity.po.AddressPOExample;
import com.atguigu.crowd.entity.po.OrderPO;
import com.atguigu.crowd.entity.po.OrderProjectPO;
import com.atguigu.crowd.entity.vo.AddressVO;
import com.atguigu.crowd.entity.vo.OrderProjectVO;
import com.atguigu.crowd.entity.vo.OrderVO;
import com.atguigu.crowd.mapper.AddressPOMapper;
import com.atguigu.crowd.mapper.OrderPOMapper;
import com.atguigu.crowd.mapper.OrderProjectPOMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ChenCheng
 * @create 2022-06-07 16:42
 */
@Transactional(readOnly = true)
@Service
public class orderServiceImpl  implements OrderService {


    @Autowired(required = false)
    private OrderProjectPOMapper orderProjectPOMapper;

    @Autowired(required = false)
    private AddressPOMapper addressPOMapper;

    @Autowired(required = false)
    private OrderPOMapper orderPOMapper;



    @Override
    public OrderProjectVO getOrderProjectVO(Integer returnId) {
      return orderProjectPOMapper.selectOrderProjectVO(returnId);
    }

    @Override
    public List<AddressVO> getAddressListVOByMemberId(Integer memberId) {
        // 1.创建查询的条件
        AddressPOExample addressPOExample = new AddressPOExample();
        AddressPOExample.Criteria criteria = addressPOExample.createCriteria();
        criteria.andMemberIdEqualTo(memberId);
        // 2.查询出符合条件的集合
        List<AddressPO> addressPOList = addressPOMapper.selectByExample(addressPOExample);

        // 3.准备一个存放vo的集合
        ArrayList<AddressVO> addressVOList = new ArrayList<>();

        // 4.将po转化为vo
        for (AddressPO addressPO:
                addressPOList
             ) {
            AddressVO addressVO = new AddressVO();
            BeanUtils.copyProperties(addressPO,addressVO);
            addressVOList.add(addressVO);
        }
        return addressVOList;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void saveAddressPO(AddressVO addressVO) {
        AddressPO addressPO = new AddressPO();
        BeanUtils.copyProperties(addressVO,addressPO);

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void saveOrder(OrderVO orderVO) {
        // 1.创建OrderPO对象
        OrderPO orderPO = new OrderPO();
        // 2.从传入的OrderVO给OrderPO赋值
        BeanUtils.copyProperties(orderVO,orderPO);
        // 3.将OrderPO存入数据库
        orderPOMapper.insert(orderPO);
        // 4.得到存入后自增产生的order id
        Integer orderId = orderPO.getId();
        // 5.得到orderProjectVO
        OrderProjectVO orderProjectVO = orderVO.getOrderProjectVO();
        // 6.创建OrderProjectPO对象
        OrderProjectPO orderProjectPO = new OrderProjectPO();
        // 7.赋值
        BeanUtils.copyProperties(orderProjectVO,orderProjectPO);
        // 8.给orderProjectPO设置orderId
        orderProjectPO.setOrderId(orderId);
        // 9.存入数据库
        orderProjectPOMapper.insert(orderProjectPO);
    }
}
