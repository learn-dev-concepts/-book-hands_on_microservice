package se.magnus.microservices.composite.product.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import se.magnus.api.core.product.Product;
import se.magnus.api.core.product.ProductService;
import se.magnus.api.core.recommendation.Recommendation;
import se.magnus.api.core.recommendation.RecommendationService;
import se.magnus.api.core.review.Review;
import se.magnus.api.core.review.ReviewService;
import se.magnus.util.exceptions.InvalidInputException;
import se.magnus.util.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.magnus.util.http.HttpErrorInfo;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Component
public class ProductCompositeIntegration implements ProductService, RecommendationService, ReviewService {

  private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeIntegration.class);
  private final RestTemplate restTemplate;
  private final ObjectMapper mapper;
  private final String productServiceUrl;
  private final String recommendationServiceUrl;
  private final String reviewServiceUrl;

  public ProductCompositeIntegration(
          RestTemplate restTemplate,
          ObjectMapper mapper,
          @Value("${app.product-service.host}") String productServiceHost,
          @Value("${app.product-service.port}") int productServicePort,
          @Value("${app.recommendation-service.host}") String recommendationServiceHost,
          @Value("${app.recommendation-service.port}") int recommendationServicePort,
          @Value("${app.review-service.host}") String reviewServiceHost,
          @Value("${app.review-service.port}") int reviewServicePort
  ) {
    this.restTemplate = restTemplate;
    this.mapper = mapper;

    productServiceUrl = "http://" + productServiceHost + ":" + productServicePort + "/product/";
    recommendationServiceUrl = "http://" + recommendationServiceHost + ":" + recommendationServicePort + "/recommendation?productId=";
    reviewServiceUrl = "http://" + reviewServiceHost + ":" + reviewServicePort + "/review?productId=";
  }


  @Override
  public Product getProduct(int productId) {
    try {
      String url = productServiceUrl + productId;
      Product product = restTemplate.getForObject(url, Product.class);
      return product;

    } catch (HttpClientErrorException ex) {

      HttpStatusCode statusCode = ex.getStatusCode();
      if (statusCode.equals(NOT_FOUND)) {
        throw new NotFoundException(getErrorMessage(ex));
      } else if (statusCode.equals(UNPROCESSABLE_ENTITY)) {
        throw new InvalidInputException(getErrorMessage(ex));
      }

      LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
      LOG.warn("Error body: {}", ex.getResponseBodyAsString());
      throw ex;
    }
  }

  private String getErrorMessage(HttpClientErrorException ex) {
    try {
      return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
    } catch (IOException ioex) {
      return ex.getMessage();
    }
  }

  @Override
  public List<Recommendation> getRecommendations(int productId) {
    String url = recommendationServiceUrl + productId;
    List<Recommendation> recommendations = restTemplate
            .exchange(url, GET, null, new ParameterizedTypeReference<List<Recommendation>>() {})
            .getBody();

    return recommendations;
  }

  @Override
  public List<Review> getReviews(int productId) {
    String url = reviewServiceUrl + productId;
    List<Review> reviews = restTemplate
            .exchange(url, GET, null, new ParameterizedTypeReference<List<Review>>() {})
            .getBody();

    return reviews;
  }
}
