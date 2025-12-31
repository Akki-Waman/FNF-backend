package com.sipl.ticket.product.service;


import com.sipl.ticket.core.dao.entity.ProductUnit;
import com.sipl.ticket.core.dao.entity.Products;
import com.sipl.ticket.core.dao.entity.Unit;
import com.sipl.ticket.core.dao.repository.ProductUnitRepository;
import com.sipl.ticket.core.dao.repository.UnitRepository;
import com.sipl.ticket.core.dto.response.ProductUnitDto;
import com.sipl.ticket.core.exception.custom.ProductUnitNotFoundException;
import com.sipl.ticket.core.mapper.ProductUnitMapper;
import com.sipl.ticket.core.util.CbmCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductUnitService {

    private final CbmCalculator cbmCalculator;
    private final ProductUnitRepository productUnitRepository;
    private final ProductUnitMapper productUnitMapper;
    private final UnitRepository unitRepository;

    public List<ProductUnitDto> saveProductUnits(
            List<ProductUnitDto> productUnitDtoList, Products product) throws Exception {

        List<ProductUnit> productUnitListToBeSaved = new ArrayList<>();
        for (int i = 0; i < productUnitDtoList.size(); i++) {
            ProductUnit currentProductUnitTobeSaved = null;

            ProductUnitDto currentProductUnitDto = productUnitDtoList.get(i);
            Double cbm =
                    cbmCalculator.calculateCbm(
                            currentProductUnitDto.getLength(),
                            currentProductUnitDto.getWidth(),
                            currentProductUnitDto.getHeight());
            currentProductUnitDto.setCbmValue(cbm);
            if (currentProductUnitDto.getProductUnitId() != null) {
                currentProductUnitTobeSaved =
                        productUnitRepository
                                .findById(currentProductUnitDto.getProductUnitId())
                                .orElseThrow(
                                        () ->
                                                new ProductUnitNotFoundException(
                                                        "ProductUnit doesn't exist for one of the productUnits sent for updation"));
                currentProductUnitTobeSaved =
                        productUnitMapper.updateExistingProductUnit(
                                currentProductUnitTobeSaved, currentProductUnitDto);
            } else {
                currentProductUnitTobeSaved = productUnitMapper.toProductUnit(currentProductUnitDto);
            }
            Optional<Unit> optionalUnit =
                    unitRepository.findActiveById(productUnitDtoList.get(i).getUnit().getUnitId());
            optionalUnit.ifPresent(currentProductUnitTobeSaved::setUnit);
            currentProductUnitTobeSaved.setProduct(product);
            productUnitListToBeSaved.add(currentProductUnitTobeSaved);
        }
        return productUnitMapper.toProductUnitDtoList(
                productUnitRepository.saveAll(productUnitListToBeSaved));
    }
}
