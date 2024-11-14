package ca.com.food.orders.service;

import ca.com.food.orders.dto.OrderDto;
import ca.com.food.orders.dto.StatusDto;
import ca.com.food.orders.model.Order;
import ca.com.food.orders.model.Status;
import ca.com.food.orders.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private final ModelMapper modelMapper;


    public List<OrderDto> getAll() {
        return repository.findAll().stream()
                .map(p -> modelMapper.map(p, OrderDto.class))
                .collect(Collectors.toList());
    }

    public OrderDto findById(Long id) {
        Order order = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return modelMapper.map(order, OrderDto.class);
    }

    public OrderDto createOrder(OrderDto dto) {
        Order order = modelMapper.map(dto, Order.class);

        order.setDateTime(LocalDateTime.now());
        order.setStatus(Status.DONE);
        order.getItens().forEach(item -> item.setOrder(order));
        Order salved = repository.save(order);

        return modelMapper.map(order, OrderDto.class);
    }

    public OrderDto updateStatus(Long id, StatusDto dto) {

        Order order = repository.byIdComItens(id);

        if (order == null) {
            throw new EntityNotFoundException();
        }

        order.setStatus(dto.getStatus());
        repository.updateStatus(dto.getStatus(), order);
        return modelMapper.map(order, OrderDto.class);
    }

    public void approvePaymentOrder(Long id) {

        Order order = repository.byIdComItens(id);

        if (order == null) {
            throw new EntityNotFoundException();
        }

        order.setStatus(Status.PAID);
        repository.updateStatus(Status.PAID, order);
    }
}
