package org.bp.payment;

import org.bp.payment.generated.CancelPaymentRequest;
import org.bp.payment.generated.MakePaymentRequest;
import org.bp.payment.generated.payment.PaymentCreation;
import org.bp.payment.generated.payment.PaymentFaultMsg;
import org.bp.payment.generated.types.PaymentInfo;
import org.springframework.stereotype.Service;
import org.bp.payment.generated.types.Fault;

@Service
public class PaymentCreationEndpoint implements PaymentCreation {

    @Override
    public PaymentInfo makePayment(MakePaymentRequest payload) throws PaymentFaultMsg {
        if (payload != null &&
                payload.getPaymentCard().getName() != null &&
                payload.getPerson().getName() != null &&
                payload.getPerson().getName().equals(payload.getPaymentCard().getName())) {
            Fault paymentFault = new Fault();
            paymentFault.setCode(400);
            paymentFault.setText("Data is incorrect");
            throw new PaymentFaultMsg("Data is incorrect", paymentFault);
        } else if ( payload.getCost() < 0f) {
            Fault paymentFault = new Fault();
            paymentFault.setCode(410);
            paymentFault.setText("The cost is negative: " + payload.getCost());
            throw new PaymentFaultMsg("Cost is negative: ", paymentFault);
        }
        PaymentInfo response = new PaymentInfo();
        response.setId(1);
        response.setCost(payload.getCost());
        return response;
    }

    @Override
    public PaymentInfo cancelPayment(CancelPaymentRequest payload) throws PaymentFaultMsg {
        return null;
    }
}
