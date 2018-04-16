package pl.sokn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.sokn.definitions.SoknDefinitions.ApiMessages;
import pl.sokn.dto.CustomResponseMessage;
import pl.sokn.dto.MailingListDTO;
import pl.sokn.entity.MailingList;
import pl.sokn.exception.OperationException;
import pl.sokn.service.MailingListService;
import pl.sokn.service.implementation.MailingListServiceImpl;

@Api(tags = "MailingList")
@RestController
@RequestMapping(path = "/mailingList")
public class MailingListController extends AbstractGenericController<MailingListDTO, MailingList, String>{

    private MailingListServiceImpl mailingListService;

    @Autowired
    public MailingListController(MailingListServiceImpl mailingListService){
        super(mailingListService);
        this.mailingListService = mailingListService;
    }

    @Override
    protected MailingListDTO convertToDTO(MailingList mailingList) {
        return MailingList.convertTo(mailingList);
    }

    @Override
    protected MailingList convertToEntity(MailingListDTO mailingListDTO) {
        return MailingList.convertFrom(mailingListDTO);
    }

    @ApiOperation(value = "Add email address to mailing list")
    @PostMapping(path="/subscribe",consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity subscribe(@RequestBody MailingListDTO mailingListDTO) throws OperationException {

        return ResponseEntity.ok(mailingListService.save(convertToEntity(mailingListDTO)));
    }

    @ApiOperation(value = "Check if email address is in mailing list")
    @PostMapping(path="/checkIfSubscribe",consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity checkIfSubscribe(@RequestBody MailingListDTO mailingListDTO) throws OperationException {

        return ResponseEntity.ok(mailingListService.retrieve(convertToEntity(mailingListDTO).getEmail()));
    }

    @ApiOperation(value = "Remove email address from mailing list")
    @PostMapping(path="/unsubscribe",consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity Unsubscribe(@RequestBody MailingListDTO mailingListDTO) throws OperationException {

        mailingListService.remove(convertToEntity(mailingListDTO));
        return ResponseEntity.status(HttpStatus.OK).body(new CustomResponseMessage<>(HttpStatus.OK,""));
    }
}
