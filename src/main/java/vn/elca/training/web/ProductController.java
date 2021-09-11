package vn.elca.training.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.elca.training.model.criteria.ProductSearchCriteria;
import vn.elca.training.model.dto.ProductDto;
import vn.elca.training.model.dto.PurchaseOrderDto;
import vn.elca.training.model.exception.EntityNotFoundException;
import vn.elca.training.model.exception.ProjectNotFoundException;
import vn.elca.training.model.response.ListResponse;
import vn.elca.training.model.response.MessageReponse;
import vn.elca.training.service.ProductService;
import vn.elca.training.service.PurchaseOrderService;
import vn.elca.training.util.StatusCode;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "http://127.0.0.1:4200")
@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping("/list")
    @ResponseBody
    public ListResponse<ProductDto> getListProduct(){
        List<ProductDto> rs = productService.getAllProduct();
        return new ListResponse<>(0, rs.size(), rs.size(), rs);
    }

    @GetMapping("/list/{page}/{size}")
    @ResponseBody
    public ListResponse<ProductDto> getListProductPaginate(@PathVariable int page, @PathVariable int size){
        return productService.getAllProductPaginate(page, size);
    }

    @PostMapping("/search/{page}/{size}")
    @ResponseBody
    public ListResponse<ProductDto> getListProductPaginate(@RequestBody ProductSearchCriteria criteria,
                                                           @PathVariable int page,
                                                           @PathVariable int size){
        return productService.searchProductByCriteria(criteria, page, size);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ProductDto getListProduct(@PathVariable Long id) throws EntityNotFoundException {
        return productService.getProductById(id);
    }

    @PostMapping("/create")
    @ResponseBody
    public ProductDto createProduct(@RequestBody @Valid ProductDto productDto){
        return productService.createProduct(productDto);
    }

    @PutMapping("/update")
    @ResponseBody
    public ProductDto updateProduct(@RequestBody @Valid ProductDto productDto){
        return productService.updateProduct(productDto);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<MessageReponse> updateProduct(@PathVariable Long id) throws EntityNotFoundException {
        boolean rs = productService.deleteProduct(id);
        MessageReponse mp;
        if (rs) {
            mp = new MessageReponse(StatusCode.DELETE_OK.getCode(), Collections.singletonList("Delete successfully product id: " + id));
        } else {
            mp = new MessageReponse(StatusCode.DELETE_NOT_FOUND.getCode(), Collections.singletonList("Product not found id: " + id));
        }
        return new ResponseEntity<>(mp, HttpStatus.OK);
    }
}
