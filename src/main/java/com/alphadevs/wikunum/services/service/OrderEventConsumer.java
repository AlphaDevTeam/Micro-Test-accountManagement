package com.alphadevs.wikunum.services.service;

import com.alphadevs.wikunum.services.config.KafkaOrderEventConsumer;
import com.alphadevs.wikunum.services.domain.CustomerAccount;
import com.alphadevs.wikunum.services.service.dto.OrderDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class OrderEventConsumer {

    private final Logger log = LoggerFactory.getLogger(OrderEventConsumer.class);

    private CustomerAccountService customerAccountService;

    public OrderEventConsumer(CustomerAccountService customerAccountService) {
        this.customerAccountService = customerAccountService;
    }

    @StreamListener(value = KafkaOrderEventConsumer.CHANNELNAME, copyHeaders = "false")
    public void consume(Message<OrderDTO> message) {
        OrderDTO dto = message.getPayload();
        log.info("Got message from kafka stream: {} {}", dto.getOrderID(), dto.getOrderNumber());
        try {
            CustomerAccount customerAccount = new CustomerAccount();
            customerAccount.setBalanceAmount(0.0);
            customerAccount.setCreditAmount(0.0);
            customerAccount.setDebitAmount(0.0);
            customerAccount.setDescription("Purchase Order Created " + dto.getOrderNumber());
            customerAccount.setLocationCode("LOCID");
            customerAccount.setTenantCode("DEMO");
            customerAccount.setTransactionID(dto.getOrderID());

            customerAccountService.save(customerAccount);

            log.info("Saved Customer Transaction {} {}", dto.getOrderID(), dto.getOrderNumber());
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}
