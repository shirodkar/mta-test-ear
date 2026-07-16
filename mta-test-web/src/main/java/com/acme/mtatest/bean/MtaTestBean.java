package com.acme.mtatest.bean;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.acme.environment.api.EnvironmentService;
import com.acme.environment.client.EnvironmentClientImpl;
import com.acme.mtatest.exception.MtaTestException;
import com.acme.mtatest.model.MtaTestRequest;
import com.acme.mtatest.model.MtaTestResponse;
import com.acme.mtatest.model.ShipperAddress;
import com.acme.mtatest.service.MtaTestService;
import com.acme.mtatest.service.RecaptchaService;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@ViewScoped
public class MtaTestBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(MtaTestBean.class);

    private final EnvironmentService environmentService = new EnvironmentClientImpl();

    @Inject
    private MtaTestService mtaTestService;

    @Inject
    private RecaptchaService recaptchaService;

    private MtaTestRequest request;
    private MtaTestResponse response;
    private String lookupConfirmationNumber;

    public MtaTestBean() {
        request = new MtaTestRequest();
        request.setShipperAddress(new ShipperAddress());
    }

    public String submitMtaTest() {
        try {
            logger.info("Submitting mtatest in environment: {}", environmentService.getEnvironmentName());
            response = mtaTestService.scheduleMtaTest(request);
            return "/pages/confirmation?faces-redirect=true";
        } catch (MtaTestException e) {
            logger.error("Failed to schedule mtatest: {}", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
            return null;
        }
    }

    public String lookupMtaTest() {
        try {
            response = mtaTestService.getMtaTestStatus(lookupConfirmationNumber);
            if (response == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "No mtatest found with that confirmation number.", null));
            }
            return null;
        } catch (Exception e) {
            logger.error("Error looking up mtatest: {}", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error looking up mtatest status.", null));
            return null;
        }
    }

    public String cancel() {
        MtaTestRequest freshRequest = new MtaTestRequest();
        freshRequest.setShipperAddress(new ShipperAddress());
        try {
            BeanUtils.copyProperties(request, freshRequest);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.warn("BeanUtils copy failed, resetting manually: {}", e.getMessage());
            request = freshRequest;
        }
        return "/index?faces-redirect=true";
    }

    public MtaTestRequest getRequest() {
        return request;
    }

    public void setRequest(MtaTestRequest request) {
        this.request = request;
    }

    public MtaTestResponse getResponse() {
        return response;
    }

    public void setResponse(MtaTestResponse response) {
        this.response = response;
    }

    public String getLookupConfirmationNumber() {
        return lookupConfirmationNumber;
    }

    public void setLookupConfirmationNumber(String lookupConfirmationNumber) {
        this.lookupConfirmationNumber = lookupConfirmationNumber;
    }
}
