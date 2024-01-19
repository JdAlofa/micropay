package dtu.dtupay;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.DELETE;
import java.util.ArrayList;

import dtu.dtupay.common.PaymentRequestPayload;
import dtu.dtupay.common.Token;
import org.apache.commons.text.StringEscapeUtils;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/")
public class DTUPayResource {

	private DTUPayService dtuPayService;

	public DTUPayResource() throws Exception {
		dtuPayService = new DTUPayService();
	}

	@GET
	@Path("/tokens/{id}/{numberOfTokensRequested}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response requestTokens(@PathParam("id") String id,
			@PathParam("numberOfTokensRequested") int numberOfTokensRequested) {
		try {
			CompletableFuture<String> futureResult = dtuPayService.requestTokens(id, numberOfTokensRequested);
			String result = futureResult.get();
			ObjectMapper objectMapper = new ObjectMapper();
			result = result.substring(1, result.length() - 1); // Remove the extra quotes

			String unescapedResult = StringEscapeUtils.unescapeJava(result);
			ArrayList<Token> tokenList = objectMapper.readValue(unescapedResult, new TypeReference<ArrayList<Token>>() {
			});

			return Response.ok(tokenList, MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			System.out.println(e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@POST
	@Path("/customers/{id}")
	public Response createCustomer(@PathParam("id") String id) {
		if (dtuPayService.customerExists(id)) {
			return Response.status(Response.Status.CONFLICT).entity("Customer already registered").build();
		} else {
			dtuPayService.registerCustomer(id);
			return Response.status(Response.Status.OK).entity("Customer registered successfully").build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/payments")
	public Response createPayment(PaymentRequestPayload payment) {
		try {
			CompletableFuture<String> futureResult = dtuPayService.requestPayment(payment);
			String result = futureResult.get();
			ObjectMapper objectMapper = new ObjectMapper();
			Boolean success = objectMapper.readValue(result, Boolean.class);

			if (success) {
				return Response.ok("payment successful", MediaType.APPLICATION_JSON).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
			}
		} catch (Exception e) {
			System.out.println(e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DELETE
	@Path("/customers/{id}")
	public Response deleteCustomer(@PathParam("id") String id) {
		if (dtuPayService.customerExists(id)) {
			dtuPayService.deregisterCustomer(id);
			return Response.status(Response.Status.OK).entity("Customer deleted successfully").build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Customer not found").build();
		}
	}

	@POST
	@Path("/merchants/{id}")
	public Response createMerchant(@PathParam("id") String id) {
		if (dtuPayService.merchantExists(id)) {
			return Response.status(Response.Status.CONFLICT).entity("Customer already registered").build();
		} else {
			dtuPayService.registerMerchant(id);
			return Response.status(Response.Status.OK).entity("Customer registered successfully").build();
		}
	}

	@DELETE
	@Path("/merchants/{id}")
	public Response deleteMerchant(@PathParam("id") String id) {
		if (dtuPayService.merchantExists(id)) {
			dtuPayService.deregisterMerchant(id);
			return Response.status(Response.Status.OK).entity("Customer deleted successfully").build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Customer not found").build();
		}
	}

}
