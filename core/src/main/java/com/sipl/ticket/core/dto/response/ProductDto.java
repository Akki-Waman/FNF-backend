package com.sipl.ticket.core.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto extends AuditDto {
    private Long productId;

    private String productCode;

    private String productDesc;

    private String productName;

    private ProductSubCategoryDto productSubCategory;

    private ProductCategoryDto productCategory;

    private BrandDto brands;

    private OriginDto origins;

    private AccountDto account;

    private Long reorderLevel;

    private Long reorderQty;

    private Double minLevel;

    private Double maxLevel;

    private Double netWeight;

    private Double grossWeight;

    private UnitDto unit;

    private Boolean withBatchTracking;

    private Boolean withExpiryDate;

    private Boolean isService = false;

    private String fileName;

    private Boolean isActive;

    private String value;

    private Integer qcCategory;

    private String hsn;

    private String productShortName;

    private String partNumber;

    private GstSlabDto defaultTaxHead;

    private Long dmsDocId;

    private Boolean isSync;
}
