package se.magnus.api.composite.product;

import org.springframework.web.bind.annotation.*;
import se.magnus.api.core.product.Product;

public interface ProductCompositeService {

    /**
     * Sample usage: curl $HOST:$PORT/product-composite/1
     *
     * @param productId
     * @return the composite product info, if found, else null
     */
    @GetMapping(
        value    = "/product-composite/{productId}",
        produces = "application/json")
    ProductAggregate getProduct(@PathVariable int productId);

    @PostMapping(
            value    = "/product-composite",
            consumes = "application/json")
    void createCompositeProduct(@RequestBody ProductAggregate body);

    @DeleteMapping(value = "/product-composite/{productId}")
    void deleteCompositeProduct(@PathVariable int productId);
}
