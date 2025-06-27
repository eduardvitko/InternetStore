package com.example.InternetStore.services;



import com.example.InternetStore.dto.OrderItemDto;
import com.example.InternetStore.model.OrderItem;
import com.example.InternetStore.model.Product;
import com.example.InternetStore.reposietories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final ProductRepository productRepository;

    public OrderItemDto mapToDto(OrderItem item) {
        return OrderItemDto.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .build();
    }

    public OrderItem mapToEntity(OrderItemDto dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return OrderItem.builder()
                .product(product)
                .quantity(dto.getQuantity())
                .price(dto.getPrice())
                .build();
    }
}
