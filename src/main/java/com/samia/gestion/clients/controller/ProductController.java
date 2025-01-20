package com.samia.gestion.clients.controller;

import com.samia.gestion.clients.DTO.ClientDTO;
import com.samia.gestion.clients.DTO.PaginationResponse;
import com.samia.gestion.clients.DTO.ProductDTO;
import com.samia.gestion.clients.service.ProductService;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/product")
    public ProductDTO createProduct(@RequestBody ProductDTO productDTO){
        return productService.createProduct(productDTO);
    }

    @GetMapping("/product/{id}")
    public ProductDTO getProductById(@PathVariable(value = "id") Long id){
        return productService.getProductById(id);
    }

    @GetMapping("/product/name/{name}")
    public ProductDTO getProductByName(@PathVariable(value = "name") String name){
        return productService.getProductByName(name);
    }

    @PutMapping("/product/{id}")
    public ProductDTO updateProduct(@RequestBody ProductDTO productDetails, @PathVariable(value = "id") Long id){
        return productService.updateProduct(productDetails, id);
    }

    @GetMapping("/products")
    @ResponseBody
    public PaginationResponse<ProductDTO> getAllProducts(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                         @RequestParam(value = "size", defaultValue = "20", required = false) int size,
                                                         @RequestParam(value = "search", required = false) String search){
        return productService.getAllProducts(page, size, search);
    }

    @DeleteMapping("/product/{id}")
    public void deleteProduct(@PathVariable(value = "id") Long id){
        productService.deleteProduct(id);
    }
}
