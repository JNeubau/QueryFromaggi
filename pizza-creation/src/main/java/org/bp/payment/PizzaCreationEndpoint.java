package org.bp.payment;

import org.bp.payment.generated.CancelPizzaRequest;
import org.bp.payment.generated.CreatePizzaRequest;
import org.bp.payment.generated.pizza.PizzaCreation;
import org.bp.payment.generated.pizza.PizzaFaultMsg;
import org.bp.payment.generated.types.PizzaInfo;
import org.springframework.stereotype.Service;

@Service
public class PizzaCreationEndpoint implements PizzaCreation {

    @Override
    public PizzaInfo cancelPizza(CancelPizzaRequest payload) throws PizzaFaultMsg {
        return null;
    }

    @Override
    public PizzaInfo createPizza(CreatePizzaRequest payload) throws PizzaFaultMsg {
        if (payload!=null && payload.getPizza()!=null
                && "ananas".equals(payload.getPizza().getIngredients())) {
            org.bp.payment.generated.types.Fault pizzaFault = new org.bp.payment.generated.types.Fault();
            pizzaFault.setCode(10);
            pizzaFault.setText("Ananas is temporarily not available ingredient");
                    PizzaFaultMsg fault = new PizzaFaultMsg("Ingredient is not available",
                            pizzaFault);
            throw fault;
        }
        PizzaInfo response = new PizzaInfo();
        response.setId(1);
        response.setCost(new java.math.BigDecimal(999));
        return response;
    }
}
