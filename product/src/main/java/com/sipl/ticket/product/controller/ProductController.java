package com.sipl.ticket.product.controller;

import com.sipl.ticket.core.dto.request.ProductSearchRequestDto;
import com.sipl.ticket.core.dto.response.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RequestMapping("/api/v1/products")
@RestController
@CrossOrigin(origins = "*")
@Api(tags = "Product APIs")
public interface ProductController {

    @ApiOperation(
            value = "Create a new product entry",
            notes = "Provide the necessary product information to save a new entry")
    @PostMapping("/save")
    public ResponseEntity<ApiResponseDTO<CombinedProductResponseDto>> addProduct(
            @RequestPart(value = "productRequestDto") String productRequestDto,
            @RequestPart(value = "image", required = false) MultipartFile multipartFile);

    @ApiOperation(
            value = "Update an existing product",
            notes = "Provide the necessary product information to save a new entry",
            response = ProductDto.class)
    @PutMapping("/update/{productId}")
    public ResponseEntity<ApiResponseDTO<CombinedProductResponseDto>> updateProduct(
            @PathVariable(value = "productId") Long productId,
            @RequestPart(value = "productRequestDto") String productRequestDto,
            @RequestPart(value = "image", required = false) MultipartFile multipartFile);


    @ApiOperation(
            value = "Delete a product by ID",
            notes = "Provide the product ID to delete the corresponding product",
            response = ProductDto.class)
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseDTO<ProductDto>> deleteProduct(@PathVariable Long productId);

    @ApiOperation(
            value = "Retrieve product details by ID",
            notes = "Provide the product ID to fetch the corresponding product details.",
            response = ProductDto.class)
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponseDTO<CombinedProductResponseDto>> getByProduct(@PathVariable Long productId);

    @ApiOperation(
            value = "Retrieve all product details",
            notes = "Fetch all the product entries available in the system.",
            response = ProductDto.class)
    @GetMapping("")
    public ResponseEntity<ApiResponseDTO<ProductDto>> getAllProduct(
            @RequestParam(required = false) Integer branchId
    );

    @ApiOperation(
            value = "Search products",
            notes = "Search and filter products using optional criteria such as product ID, brand, origin, category, and sub-category with pagination and sorting support.",
            response = ProductDto.class
    )
    @PostMapping("/search")
    public ResponseEntity<ApiResponseDTO<PagedResponse<CombinedProductResponseDto>>> searchProducts(
            @RequestBody ProductSearchRequestDto requestDto
    );

    @GetMapping("/export")
    ResponseEntity<Void> exportProductsExcel(HttpServletResponse response,
                                             @RequestParam(required = false) Integer branchId
    );

    @ApiOperation(
            value = "Download product file",
            notes = "Downloads product related file by file name",
            response = byte[].class)
    @GetMapping(
            value = "/download/{fileName}",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    ResponseEntity<?> downloadProductFile(
            @PathVariable("fileName") String fileName);

    @ApiOperation(
            value = "Upload excel sheet of products and unit and save in products and products unit table.",
            notes = "Upload excel sheet of products and unit and save in products and products unit table.")
    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    ResponseEntity<ApiResponseDTO<Void>> uploadFile(
            @ApiParam(value = "Excel file", required = true)
            @RequestPart("file") MultipartFile file);



}
