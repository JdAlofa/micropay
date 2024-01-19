package dtu.dtupay;

import dtu.dtupay.common.Customer;
import dtu.dtupay.common.Payment;
import java.math.BigDecimal;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.DELETE;

import java.util.concurrent.CompletableFuture;

@Path("/")
public class DTUPayResource {

	private DTUPayService dtuPayService;

	public DTUPayResource() throws Exception {
		dtuPayService = new DTUPayService();
	}

	@POST
	@Path("/hellorabbit/{msg}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response helloRabbit(@PathParam("msg") String msg) {
		try {
			CompletableFuture<String> futureResult = dtuPayService.sayHello(msg);
			String result = futureResult.get();

			return Response.ok(result, MediaType.TEXT_PLAIN).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GET
	@Path("/tokens/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response requestTokens(@PathParam("id") String id) {
		try {
			CompletableFuture<String> futureResult = dtuPayService.requestTokens(id);
			String result = futureResult.get();

			System.out.println(result);
			return Response.ok(result, MediaType.TEXT_PLAIN).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GET
	@Path("/customers")
	@Produces(MediaType.APPLICATION_JSON)
	public Customer getCustomers() {
		var customer = new Customer("testid");
		return customer;

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

	@POST
	@Path("/payments")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response transferMoney(Payment payment) {

		String customerId = payment.getCustomerId();
		String merchantId = payment.getMerchantId();
		BigDecimal amount = payment.getAmount();

		try {
			dtuPayService.transferMoney(customerId, merchantId, amount);
			return Response.ok(Response.Status.OK).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
}
