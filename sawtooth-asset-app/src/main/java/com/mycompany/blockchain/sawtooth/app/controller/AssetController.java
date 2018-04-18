package com.mycompany.blockchain.sawtooth.app.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParser;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.googlecode.protobuf.format.JsonFormat;
import com.mycompany.blockchain.sawtooth.app.vo.AssetVO;
import com.mycompany.blockchain.sawtooth.client.asset.AssetPayloadClientService;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.Asset;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.AssetPayload;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.AssetPayload.PayloadType;
import com.mycompany.blockchain.sawtooth.mortgage.asset.protobuf.CreateAsset;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import sawtooth.sdk.processor.exceptions.ValidatorConnectionError;
import sawtooth.sdk.protobuf.ClientBatchSubmitResponse.Status;


/**
 * 
 * Provided rest controllers for asset services.
 * 
 * @author Nishant Sonar
 *
 */
@Controller
@Slf4j
public class AssetController {

	@Autowired
	AssetPayloadClientService assetPayloadService;
	
	@RequestMapping(consumes="application/json",method=RequestMethod.GET,path="/asset")
	public @ResponseBody String getAsset(@RequestParam String name) throws InvalidProtocolBufferException, InterruptedException, ValidatorConnectionError, UnsupportedEncodingException {
		Asset entity = Asset.newBuilder().setName(name).build();
		String address = assetPayloadService.getiAddressBuilder().buildAddress(entity);
		ByteString result = assetPayloadService.getTemplate().getClientGetStateRequest(address);
		String strin = JsonFormat.printToString(Asset.parseFrom(result));
		log.info("JSON : ",strin);
		return strin;
	}
	
	@RequestMapping(consumes="application/json",method=RequestMethod.POST,path="/asset")
	public @ResponseBody int putAsset(@RequestBody AssetVO asset) throws Exception {
		CreateAsset creteAsset = CreateAsset.newBuilder().setName(asset.getName()).setValue(asset.getValue()).setDetails(asset.getDetails()).setPubKey(asset.getOwner()).build(); 
		AssetPayload payload = AssetPayload.newBuilder().setPayloadType(PayloadType.CREATE_ASSET).setCreateAsset(creteAsset).build();
		Status response = assetPayloadService.submitStateChange(payload);
		if(!response.equals(Status.OK) && !response.equals(Status.STATUS_UNSET)) {
			log.warn("Response for submission is : "+ response.getNumber());		
		}
		return response.getNumber();
	}
}
