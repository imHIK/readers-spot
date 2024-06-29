package org.bigBrotherBooks.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.bigBrotherBooks.dto.StockDTO;
import org.bigBrotherBooks.dto.WarehouseDTO;
import org.bigBrotherBooks.service.WarehouseService;

import java.util.List;


@Path("/warehouse")
public class WarehouseRestApi {

    private final WarehouseService warehouseService;

    @Inject
    public WarehouseRestApi(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @POST
    @Path("/")
    public Response addWarehouse(@Valid WarehouseDTO warehouseDTO) {
        warehouseService.saveWarehouse(warehouseDTO);
        return Response.ok("Warehouse " + warehouseDTO.getName() + " saved successfully.").build();
    }

    @GET
    @Path("/{warehouse_id}")
    public Response getWarehouse(@PathParam("warehouse_id") int warehouseId) {
        WarehouseDTO warehouseDTO = warehouseService.getWarehouseDTO(warehouseId);
        if (warehouseDTO == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Warehouse not found").build();
        }
        return Response.ok(warehouseDTO).build();
    }

    @DELETE
    @Path("/{warehouse_id}")
    public Response deleteWarehouse(@PathParam("warehouse_id") int warehouseId) {
        if (warehouseService.deleteWarehouse(warehouseId)) {
            return Response.ok("Warehouse deleted successfully").build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Warehouse not found").build();
    }

    @PUT
    @Path("/")
    public Response updateWarehouse(@Valid WarehouseDTO warehouseDTO) {
        if (warehouseService.updateWarehouse(warehouseDTO)) {
            return Response.ok("Warehouse updated successfully").build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Warehouse not found").build();
    }

    @GET
    @Path("/all")
    public Response getAllWarehouses() {
        List<WarehouseDTO> warehouseDTOs = warehouseService.getAllWarehouses();
        return Response.ok(warehouseDTOs).build();
    }

    @GET
    @Path("/stock/{warehouse_id}")
    public Response getStock(@PathParam("warehouse_id") int warehouseId) {
        List<StockDTO> stockDTOs = warehouseService.getWarehouseStock(warehouseId);
        if (stockDTOs == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Warehouse not found").build();
        }
        return Response.ok(stockDTOs).build();
    }

    @POST
    @Path("/stock/{warehouse_id}")
    public Response addStock(@PathParam("warehouse_id") int warehouseId, @Valid List<StockDTO> stockDTOs) {
        if (warehouseService.addWarehouseStock(warehouseId, stockDTOs)) {
            return Response.ok("Stock added successfully").build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Warehouse or Books not found").build();
    }

    @DELETE
    @Path("/stock/{warehouse_id}")
    public Response removeStock(@PathParam("warehouse_id") int warehouseId, @Valid StockDTO stockDTO) {
        if (warehouseService.removeWarehouseStock(warehouseId, stockDTO)) {
            return Response.ok("Stock removed successfully").build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Warehouse Stock not found").build();
    }

}
