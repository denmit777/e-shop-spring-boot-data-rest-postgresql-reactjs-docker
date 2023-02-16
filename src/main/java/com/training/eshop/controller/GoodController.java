package com.training.eshop.controller;

import com.training.eshop.dto.GoodAdminCreationDto;
import com.training.eshop.dto.GoodAdminViewDto;
import com.training.eshop.dto.GoodBuyerDto;
import com.training.eshop.model.Good;
import com.training.eshop.service.GoodService;;
import com.training.eshop.service.ValidationService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(value = "/goods")
@AllArgsConstructor
public class GoodController {
    private static final Logger LOGGER = LogManager.getLogger(GoodController.class.getName());

    private final GoodService goodService;
    private final ValidationService validationService;

    @PostMapping("/forAdmin")
    public ResponseEntity<?> save(@RequestBody @Valid GoodAdminCreationDto goodDto, Principal principal) {

        Good savedGood = goodService.save(goodDto, principal.getName());

        String currentUri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
        String savedGoodLocation = currentUri + "/" + savedGood.getId();

        return ResponseEntity.status(CREATED)
                .header(HttpHeaders.LOCATION, savedGoodLocation)
                .body(savedGood);
    }

    @GetMapping("/forAdmin")
    public ResponseEntity<?> getAllForAdmin(@RequestParam(value = "searchField", defaultValue = "default") String searchField,
                                            @RequestParam(value = "parameter", defaultValue = "") String parameter,
                                            @RequestParam(value = "sortField", defaultValue = "id") String sortField,
                                            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection,
                                            @RequestParam(value = "pageSize", defaultValue = "25") int pageSize,
                                            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber) {
        String errorMessage = validationService.getWrongSearchParameterError(parameter);

        if (checkErrors(errorMessage)) {
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        List<GoodAdminViewDto> goods = goodService.getAllForAdmin(searchField, parameter, sortField,
                sortDirection, pageSize, pageNumber);

        return ResponseEntity.ok(goods);
    }

    @GetMapping("/forBuyer")
    public ResponseEntity<List<GoodBuyerDto>> getAllForBuyer() {
        List<GoodBuyerDto> goods = goodService.getAllForBuyer();

        LOGGER.info("All goods : {}", goods);


        return ResponseEntity.ok(goods);
    }

    @GetMapping("/forAdmin/{id}")
    public ResponseEntity<GoodAdminViewDto> getById(@PathVariable("id") Long id) {
        GoodAdminViewDto goodDto = goodService.getById(id);

        return ResponseEntity.ok(goodDto);
    }

    @PutMapping("/forAdmin/{id}")
    public ResponseEntity<?> update(Principal principal, @PathVariable("id") Long goodId,
                                    @Valid @RequestBody GoodAdminCreationDto goodDto) {
        Good updatedGood = goodService.update(goodId, goodDto, principal.getName());

        return ResponseEntity.ok(updatedGood);
    }

    @DeleteMapping("/forAdmin/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long goodId, Principal principal) {
        goodService.deleteById(goodId, principal.getName());

        return ResponseEntity.ok().build();
    }

    @GetMapping("forAdmin/total")
    public ResponseEntity<?> getTotalAmount() {
        return ResponseEntity.ok(goodService.getTotalAmount());
    }

    private boolean checkErrors(String errorMessage) {
        return !errorMessage.isEmpty();
    }
}
