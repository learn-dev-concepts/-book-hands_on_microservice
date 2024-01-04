package se.magnus.microservices.core.product.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface ProductRepository extends ReactiveMongoRepository<ProductEntity, String> {
    Mono<ProductEntity> findByProductId(int productId);
}
