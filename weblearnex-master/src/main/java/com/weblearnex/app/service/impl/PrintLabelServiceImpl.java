package com.weblearnex.app.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.constant.PaymentType;
import com.weblearnex.app.datatable.reposatory.*;
import com.weblearnex.app.entity.manifest.ThreeplManifest;
import com.weblearnex.app.entity.master.ServicablePincode;
import com.weblearnex.app.entity.order.SaleOrder;
import com.weblearnex.app.entity.setup.Client;
import com.weblearnex.app.entity.setup.ClientWarehouse;
import com.weblearnex.app.entity.setup.Courier;
import com.weblearnex.app.reposatory.ServicablePincodeRepository;
import com.weblearnex.app.service.PrintLabelService;
import com.weblearnex.app.utils.SharedMethords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.cdi.Eager;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class PrintLabelServiceImpl implements PrintLabelService {

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private SaleOrderRepository saleOrderRepository;

    @Autowired
    private ThreeplManifestRepository threeplManifestRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientWarehouseRepository clientWarehouseRepository;

    @Autowired
    private ServicablePincodeRepository servicablePincodeRepository;

    @Autowired
    @Qualifier("applicionConfig")
    private MessageSource applicionConfig;

    @Override
    public ResponseEntity<Resource> generateOrderPrintLabel(String awbNumber) {
        try {
            List<SaleOrder> saleOrderLits = saleOrderRepository.findByReferanceNoIn(Arrays.asList(awbNumber.split(",")));
            String filePath = applicionConfig.getMessage(AppProperty.ORDER_PRINT_LABEL_PATH, null, null);
            String fullFilePath =filePath+"/printLabel.pdf";
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fullFilePath));
            document.open();
            generatePDF(document,saleOrderLits,writer);
            document.close();

            ByteArrayOutputStream baos = convertPDFToByteArray(fullFilePath);

            byte [] byteArray = baos.toByteArray();
            return ResponseEntity.ok().contentLength(byteArray.length)
                    .contentType(MediaType.parseMediaType("application/pdf"))
                    .cacheControl(CacheControl.noCache())
                    .header("Content-Disposition", "attachment; filename=PrintLable.pdf")
                    .body(new ByteArrayResource(byteArray));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<Resource> generateInvoicePrintLabel(String awbNumber) {
        try {
            List<SaleOrder> saleOrderLits = saleOrderRepository.findByReferanceNoIn(Arrays.asList(awbNumber.split(",")));
            String filePath = applicionConfig.getMessage(AppProperty.ORDER_PRINT_LABEL_PATH, null, null);
            // TODO change file name of current login user.
            String fullFilePath =filePath+"/printLabel.pdf";
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fullFilePath));
            document.open();
            generateInvoicePDF(document,saleOrderLits,writer);
            document.close();

            ByteArrayOutputStream baos = convertPDFToByteArray(fullFilePath);

            byte [] byteArray = baos.toByteArray();
            return ResponseEntity.ok().contentLength(byteArray.length)
                    .contentType(MediaType.parseMediaType("application/pdf"))
                    .cacheControl(CacheControl.noCache())
                    .header("Content-Disposition", "attachment; filename=PrintLable.pdf")
                    .body(new ByteArrayResource(byteArray));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<Resource> generateManifestPrintLabel(String manifestNumber) {
        try {
            List<ThreeplManifest> manifestLits = threeplManifestRepository.findByManifestNumberIn(Arrays.asList(manifestNumber.split(",")));
            String filePath = applicionConfig.getMessage(AppProperty.ORDER_PRINT_LABEL_PATH, null, null);
            String fullFilePath =filePath+"/printLabel.pdf";
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fullFilePath));
            document.open();
            generateManifestPDF(document,manifestLits,writer);
            document.close();

            ByteArrayOutputStream baos = convertPDFToByteArray(fullFilePath);

            byte [] byteArray = baos.toByteArray();
            return ResponseEntity.ok().contentLength(byteArray.length)
                    .contentType(MediaType.parseMediaType("application/pdf"))
                    .cacheControl(CacheControl.noCache())
                    .header("Content-Disposition", "attachment; filename=PrintLable.pdf")
                    .body(new ByteArrayResource(byteArray));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private void generatePDF(Document document, List<SaleOrder> saleOrderLits, PdfWriter writer) throws Exception {
        try {
            Courier courier = null;
            if(saleOrderLits != null && !saleOrderLits.isEmpty()){
                for(SaleOrder saleOrder :  saleOrderLits){
                    courier = courierRepository.findByCourierCode(saleOrder.getCourierCode());
                    if(courier == null){
                        continue;
                    }
                    courierCartSimplePrint(document, writer, saleOrder, courier);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateInvoicePDF(Document document, List<SaleOrder> saleOrderLits, PdfWriter writer) throws Exception {
        try {
            Courier courier = null;
            if(saleOrderLits != null && !saleOrderLits.isEmpty()){
                for(SaleOrder saleOrder :  saleOrderLits){
                    courier = courierRepository.findByCourierCode(saleOrder.getCourierCode());
                    courierCartInvoicePrint(document, writer, saleOrder, courier);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateManifestPDF(Document document, List<ThreeplManifest> manifestLits, PdfWriter writer) throws Exception {
        try {
            Courier courier = null;
            if(manifestLits != null && !manifestLits.isEmpty()){
                for(ThreeplManifest threeplManifest :  manifestLits){
                    courier = courierRepository.findByCourierCode(threeplManifest.getCourierCode());
                    courierCartManifestPrint(document, writer,threeplManifest, courier);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void courierCartSimplePrint(Document document,PdfWriter writer, SaleOrder saleOrder, Courier courier) throws Exception{
        try {
            PdfContentByte cb = writer.getDirectContent();
            BaseFont bf = FontFactory.getFont("Arial", 7, Font.BOLD).getCalculatedBaseFont(false);
            BaseFont nf = FontFactory.getFont("Arial", 7, Font.NORMAL).getCalculatedBaseFont(false);
            Image image128 = null;
            Barcode128 code128 = null;
            Image image = null;
            String logoImagePath = "";
            ColumnText ct = null;
            Rectangle two = new Rectangle(288, 432);
            document.setPageSize(two);
            document.newPage();
            cb = writer.getDirectContent();


            CMYKColor magentaColor = new CMYKColor(0.f, 1.f, 0.f, 0.f);
            /*cb.setColorStroke(magentaColor);
            cb.moveTo(10, 95);
            cb.lineTo(279, 95);*/

            cb.setColorStroke(magentaColor);
            cb.moveTo(10, 270);
            cb.lineTo(279, 270);

            cb.setColorStroke(magentaColor);
            cb.moveTo(10, 330);
            cb.lineTo(279, 330);
            //cb.lineTo(559, 36);
            //cb.lineTo(559, 806);
            //float[] columnWidths = {0.5f, 0.5f,0.5f};
            //Font qFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 10);
            Font fontFormat=new Font();
            fontFormat.setSize(10);
            fontFormat.setFamily("Arial");
            fontFormat.setStyle(Font.BOLD);

            Font fontSize=new Font();
            fontSize.setSize(8);
            fontSize.setFamily("Arial");
            fontSize.setStyle(Font.BOLD);

            PdfPTable table = new PdfPTable(4);
            table.setTotalWidth(270);



            PdfPCell sku = new PdfPCell();
            Paragraph p = new Paragraph("SKU",fontFormat);
            p.setAlignment(Element.ALIGN_MIDDLE);
            sku.addElement(p);


            PdfPCell item = new PdfPCell();
            Paragraph pitem = new Paragraph("Product",fontFormat);
            pitem.setAlignment(Element.ALIGN_MIDDLE);
            item.addElement(pitem);

            PdfPCell qty = new PdfPCell();
            Paragraph qtyitem = new Paragraph("Qty",fontFormat);
            pitem.setAlignment(Element.ALIGN_MIDDLE);
            qty.addElement(qtyitem);

            PdfPCell amount = new PdfPCell();
            Paragraph amountitem = new Paragraph("Price",fontFormat);
            pitem.setAlignment(Element.ALIGN_MIDDLE);
            amount.addElement(amountitem);

            PdfPCell skuNo = new PdfPCell(new Paragraph(saleOrder.getProductSKU().toString(),fontSize));
            PdfPCell itemValue = new PdfPCell(new Paragraph(saleOrder.getProductName(),fontSize));
            PdfPCell itemQty = new PdfPCell(new Paragraph(saleOrder.getProductQuantity().toString(),fontSize));
            PdfPCell itemAmount = new PdfPCell(new Paragraph(saleOrder.getProductPrice().toString(),fontSize));

            PdfPCell blankOne = new PdfPCell(new Paragraph(""));
            PdfPCell totalOrder = new PdfPCell(new Paragraph("Order Total",fontSize));
            PdfPCell blankTwo = new PdfPCell(new Paragraph(""));
            PdfPCell totalAmount = new PdfPCell(new Paragraph(saleOrder.getProductPrice().toString(),fontSize));

            table.addCell(sku);
            table.addCell(item);
            table.addCell(qty);
            table.addCell(amount);

            table.addCell(skuNo);
            table.addCell(itemValue);
            table.addCell(itemQty);
            table.addCell(itemAmount);
            table.addCell(blankOne);
            table.addCell(totalOrder);
            table.addCell(blankTwo);
            table.addCell(totalAmount);
            table.writeSelectedRows(
                    0, -1, two.getLeft(10), two.getTop(225), writer.getDirectContent());

            //TODO RESOLVE LOGO IMAGE
            try {
                if(courier.getViewLogoAtLabel() != null && courier.getViewLogoAtLabel()){
                    //image = Image.getInstance(ClassLoader.getSystemResource("images/logo.png"));
                    image = Image.getInstance(getClass().getResource("/images/logo.png"));
                    image.scaleAbsolute(100f, 50f);
                    image.setAbsolutePosition(170f, 360f);
                    document.add(image);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            /*image = Image.getInstance(ClassLoader.getSystemResource("images/logo.png"));
            image.scaleAbsolute(70f, 40f);
            image.setAbsolutePosition(190f, 360f);
            document.add(image);*/

            cb.beginText();
            cb.setFontAndSize(bf,7);
            cb.setTextMatrix(13,410);  //220
            cb.showText("Delivered To:");
            cb.endText();

            cb.beginText();
            cb.setFontAndSize(bf, 7);
            cb.setTextMatrix(13, 400);//310
            String consigneeName = saleOrder.getConsigneeName() != null ? saleOrder.getConsigneeName() : "";
            cb.showText(consigneeName);
            cb.endText();

            StringBuffer consigneeDetails = new StringBuffer();
            consigneeDetails.append(saleOrder.getConsigneeAddress() != null ? saleOrder.getConsigneeAddress()+"\n" :"");
            consigneeDetails.append(saleOrder.getConsigneeCity() != null ? saleOrder.getConsigneeCity()+"," :"");
            consigneeDetails.append(saleOrder.getConsigneeState() != null ? saleOrder.getConsigneeState()+"," : "");
            consigneeDetails.append(saleOrder.getConsigneeCountry() != null ? saleOrder.getConsigneeCountry()+"," : "");
            consigneeDetails.append(saleOrder.getConsigneePinCode() != null ? saleOrder.getConsigneePinCode() : "");
            String consigneeaddress = consigneeDetails.toString();




            /*String consigneeAddress = saleOrder.getConsigneeAddress() != null ? saleOrder.getConsigneeAddress() : "";
            String consigneeCity = saleOrder.getConsigneeCity() != null ? saleOrder.getConsigneeCity() : "";
            String consigneeState = saleOrder.getConsigneeState() != null ? saleOrder.getConsigneeState() : "";
            String consigneeCountry = saleOrder.getConsigneeCountry() != null ? saleOrder.getConsigneeCountry() : "";
            String consigneePinCode = saleOrder.getConsigneePinCode() != null ? saleOrder.getConsigneePinCode() : "";
            String address= consigneeAddress+consigneeCity+consigneeState+consigneeCountry+","+consigneePinCode;*/
            ct = new ColumnText(cb);


            ct.setSimpleColumn(
                    new Phrase(new Chunk(consigneeaddress.toUpperCase(),FontFactory.getFont("Arial", 7, Font.NORMAL))),//Font.NORMAL
                    13, 398, 170, 20, 10, Element.ALIGN_LEFT | Element.ALIGN_TOP);//138
            ct.go();
            cb.beginText();
            cb.setFontAndSize (nf, 7);
            cb.setTextMatrix(13, 340);//310
            String consigneeMobile = saleOrder.getConsigneeMobileNumber() != null ? saleOrder.getConsigneeMobileNumber() : "";
            cb.showText("MOBILE NO:");
            cb.endText();

            cb.beginText();
            cb.setFontAndSize (bf, 7);
            cb.setTextMatrix(60, 340);//310
            cb.showText(consigneeMobile);
            cb.endText();


            cb.beginText();
            cb.setFontAndSize (nf, 7);
            cb.setTextMatrix(13, 315);//310
            //Date ordarDate = saleOrder.getOrderDate();
            String ordarDate = SharedMethords.getOnlyDate(saleOrder.getOrderDate());
            cb.showText("Order Date:");
            cb.endText();

            cb.beginText();
            cb.setFontAndSize (bf, 7);
            cb.setTextMatrix(50, 315);//310
            cb.showText(ordarDate);
            cb.endText();


            cb.beginText();
            cb.setFontAndSize (nf, 7);
            cb.setTextMatrix(13, 305);
            String InvoiceNo = saleOrder.getReferanceNo();
            cb.showText("Invoice No:"+InvoiceNo);
            cb.endText();

            cb.beginText();
            cb.setFontAndSize (nf, 7);
            cb.setTextMatrix(190,322);//310
            cb.showText("CCT Awb Number");
            cb.endText();
            code128 = new Barcode128();
            code128.setCode(InvoiceNo);
            image128 = code128.createImageWithBarcode(cb, null, null);
            image128.scaleAbsolute(100f,45f);
            image128.setAbsolutePosition(170f,270f);//320
            document.add((image128));

            cb.beginText();
            cb.setFontAndSize(nf, 18);
            cb.setTextMatrix(40, 250);
            if(PaymentType.COD.equals(saleOrder.getPaymentType())){
                cb.showText(PaymentType.COD.name());
            }else{
                cb.showText(PaymentType.PREPAID.name());
            }
            cb.endText();
            Double codAmount = saleOrder.getCodAmount();
            cb.beginText();
            cb.setFontAndSize(nf,18);
            cb.setTextMatrix(50, 230);
            cb.setTextMatrix(50, 230);
            cb.showText("₹"+codAmount);
            cb.endText();

            cb.setColorStroke(magentaColor);
            cb.moveTo(120, 267);
            cb.lineTo(120, 210);

            String CourierAwb = saleOrder.getCourierAWBNumber() != null ? saleOrder.getCourierAWBNumber() : "";
            cb.beginText();
            cb.setFontAndSize(nf,7);
            cb.setTextMatrix(170, 260f);
            String courierName = saleOrder.getCourierCode() != null ? saleOrder.getCourierCode() : "";
            cb.showText(courierName);
            //cb.showText("Courier Awb Number ");
            cb.endText();
            code128 = new Barcode128();
            code128.setCode(CourierAwb);
            image128 = code128.createImageWithBarcode(cb, null, null);
            image128.scaleAbsolute(150f,45f);
            image128.setAbsolutePosition(130f,210f);//320
            document.add((image128));


            PdfPTable Table2 = new PdfPTable(2);
            Table2.setTotalWidth(270);

            PdfPCell dimensions = new PdfPCell();
            Paragraph dimensionsItem = new Paragraph("DIMENSIONS :"+saleOrder.getLength()+" X "+saleOrder.getBreadth()+" X "+saleOrder.getHight()+"\nWEIGHT :"+ saleOrder.getWeight(),fontSize);
            dimensionsItem.setAlignment(Element.ALIGN_MIDDLE);
            dimensionsItem.setAlignment(Element.ALIGN_MIDDLE);
            dimensions.addElement(dimensionsItem);


            ServicablePincode servicablePincode = servicablePincodeRepository.findByPinCodeAndCourierCode(saleOrder.getConsigneePinCode(),saleOrder.getCourierCode());
            String routeCode = servicablePincode.getRouteCode() !=null ? servicablePincode.getRouteCode() : " ";

            PdfPCell rootCode = new PdfPCell();
            Paragraph rootCodeItem = new Paragraph("Route Code:"+routeCode,fontSize);
            rootCodeItem.setAlignment(Element.ALIGN_MIDDLE);
            rootCode.addElement(rootCodeItem);


            Table2.addCell(dimensions);
            Table2.addCell(rootCode);

            Table2.writeSelectedRows(
                    0, -1, two.getLeft(10), two.getTop(300), writer.getDirectContent());

            /*StringBuffer senderDetails = new StringBuffer();
            senderDetails.append(saleOrder.getSenderAddress() != null ? saleOrder.getSenderAddress()+" " :"");
            senderDetails.append(saleOrder.getSenderCity() != null ? saleOrder.getSenderCity()+" " :"");
            senderDetails.append(saleOrder.getSenderState() != null ? saleOrder.getSenderState()+"\n" : "");
            senderDetails.append(saleOrder.getSenderCountry() != null ? saleOrder.getSenderCountry()+"\n" : "");
            senderDetails.append(saleOrder.getSenderPinCode() != null ? saleOrder.getSenderPinCode()+"\n" : "");*/
            // String pickupAddress = senderDetails.toString();

            ClientWarehouse clientWarehouse = clientWarehouseRepository.findByWarehouseCode(saleOrder.getPickupLocationId());
            StringBuffer senderDetails = new StringBuffer();
            if(clientWarehouse != null){
                senderDetails.append(clientWarehouse.getWarehouseName() != null ? clientWarehouse.getWarehouseName()+", " :"");
                senderDetails.append(clientWarehouse.getAddress() != null ? clientWarehouse.getAddress()+"\n " :"");
                senderDetails.append(clientWarehouse.getCity() != null ? clientWarehouse.getCity()+"," : "");
                senderDetails.append(clientWarehouse.getState() != null ? clientWarehouse.getState()+"," : "");
                senderDetails.append(clientWarehouse.getCountry() != null ? clientWarehouse.getCountry()+"," : "");
                senderDetails.append(clientWarehouse.getPinCode() != null ? clientWarehouse.getPinCode() : "");
            }
            String pickupAddress = senderDetails.toString();
            cb.beginText();
            cb.setFontAndSize(bf,7);
            cb.setTextMatrix(13,75);  //220
            cb.showText("Pickup and Return Address :");
            cb.endText();

            ct = new ColumnText(cb);
            ct.setSimpleColumn(
                    new Phrase(new Chunk(pickupAddress.toUpperCase(),FontFactory.getFont("Arial", 7, Font.NORMAL))),//Font.NORMAL
                    13, 75, 170, 20, 10, Element.ALIGN_LEFT | Element.ALIGN_TOP);//138
            ct.go();

            String clientOrderId = saleOrder.getClientOrderId() != null ? saleOrder.getClientOrderId() : "";
            cb.beginText();
            cb.setFontAndSize(nf,7);
            cb.setTextMatrix(190, 60f);
            cb.showText("Client Order Id");
            cb.endText();
            code128 = new Barcode128();
            code128.setCode(clientOrderId);
            image128 = code128.createImageWithBarcode(cb, null, null);
            image128.scaleAbsolute(100f,45f);
            image128.setAbsolutePosition(170f,10f);//320
            document.add((image128));


        } catch (Exception ex) {
        ex.printStackTrace();

        }

    }

    private void courierCartInvoicePrint(Document document,PdfWriter writer, SaleOrder saleOrder, Courier courier) throws Exception{
        try {
            PdfContentByte cb = writer.getDirectContent();
            BaseFont bf = FontFactory.getFont("Arial Black", 7, Font.BOLD).getCalculatedBaseFont(false);
            BaseFont nf = FontFactory.getFont("Arial", 7, Font.NORMAL).getCalculatedBaseFont(false);
            Image image128 = null;
            Barcode128 code128 = null;
            Image image = null;
            String logoImagePath = "";
            ColumnText ct = null;
            Rectangle two = new Rectangle(595.0F, 842.0F);
            document.setPageSize(two);
            document.newPage();
            cb = writer.getDirectContent();

            //TODO RESOLVE LOGO IMAGE
            try {
                // image = Image.getInstance(ClassLoader.getSystemResource("images/logo.png"));
                image = Image.getInstance(getClass().getResource("/images/logo.png"));
                image.scaleAbsolute(270f, 65f);
                image.setAbsolutePosition(170f, 750f);
                document.add(image);
            }catch (Exception e){
                e.printStackTrace();
            }

            //String clientName = saleOrder.getClientCode() != null ? saleOrder.getClientCode() : "";
            Client client=clientRepository.findByClientCode(saleOrder.getClientCode());
            String clientName = client.getClientName() != null ? client.getClientName() : "";
            cb.beginText();
            cb.setFontAndSize(bf,14);
            cb.setTextMatrix(230,730);  //220
            cb.showText(clientName);
            cb.endText();

            CMYKColor magentaColor = new CMYKColor(0.f, 1.f, 0.f, 0.f);
            cb.setColorStroke(magentaColor);
            cb.moveTo(35, 720);
            cb.lineTo(550, 720);

            cb.beginText();
            cb.setFontAndSize(bf,22);
            cb.setTextMatrix(235,696);  //220
            cb.showText("TAX INVOICE");
            cb.endText();

            CMYKColor magentaColors = new CMYKColor(50.f, 22.f, 2.f, 40.f);
            cb.setColorStroke(magentaColors);
            cb.moveTo(35, 687);
            cb.lineTo(550, 687);


            cb.setColorStroke(magentaColors);
            cb.moveTo(35, 408);
            cb.lineTo(550, 408);

            cb.setColorStroke(magentaColor);
            cb.moveTo(280, 375);
            cb.lineTo(550, 375);

            cb.setColorStroke(magentaColor);
            cb.moveTo(35, 347);
            cb.lineTo(550, 347);


            cb.beginText();
            cb.setFontAndSize(bf,6);
            cb.setTextMatrix(37,422);  //220
            cb.showText("S.NO.");
            cb.endText();

            cb.beginText();
            cb.setFontAndSize(bf,6);
            cb.setTextMatrix(38,395);  //220
            cb.showText("1");
            cb.endText();

            cb.beginText();
            cb.setFontAndSize(bf,6);
            cb.setTextMatrix(70,422);  //220
            cb.showText("PRODUCT NAME");
            cb.endText();

            String productName = saleOrder.getProductName() != null ? saleOrder.getProductName() : "";
            cb.beginText();
            cb.setFontAndSize(bf,6);
            cb.setTextMatrix(70,395);  //220
            cb.showText(productName);
            cb.endText();

            cb.beginText();
            cb.setFontAndSize(bf,6);
            cb.setTextMatrix(130,422);  //220
            cb.showText("SKU");
            cb.endText();

            String productsku = saleOrder.getProductSKU() != null ? saleOrder.getProductSKU() : "";
            cb.beginText();
            cb.setFontAndSize(bf,6);
            cb.setTextMatrix(130,395);  //220
            cb.showText(productsku);
            cb.endText();

            cb.beginText();
            cb.setFontAndSize(bf,6);
            cb.setTextMatrix(190,422);  //220
            cb.showText("HSN");
            cb.endText();

            cb.beginText();
            cb.setFontAndSize(bf,6);
            cb.setTextMatrix(190,395);  //220
            cb.showText(" ");
            cb.endText();

            cb.beginText();
            cb.setFontAndSize(bf,6);
            cb.setTextMatrix(210,422);  //220
            cb.showText("QTY");
            cb.endText();

            Integer productQty = saleOrder.getProductQuantity() != null ? saleOrder.getProductQuantity() : 0;
            cb.beginText();
            cb.setFontAndSize(bf,6);
            cb.setTextMatrix(220,395);  //220
            cb.showText(productQty.toString());
            cb.endText();

            cb.beginText();
            cb.setFontAndSize(bf,6);
            cb.setTextMatrix(250,422);  //220
            cb.showText("UNIT PRICE");
            cb.endText();

            Double productPrice = saleOrder.getProductPrice() != null ? saleOrder.getProductPrice() : 0.0;
            cb.beginText();
            cb.setFontAndSize(bf,6);
            cb.setTextMatrix(250,395);  //220
            cb.showText(productPrice.toString());
            cb.endText();

            cb.beginText();
            cb.setFontAndSize(bf,6);
            cb.setTextMatrix(300,422);  //220
            cb.showText("UNIT DISCOUNT");
            cb.endText();

            cb.beginText();
            cb.setFontAndSize(bf,6);
            cb.setTextMatrix(300,395);  //220
            cb.showText("0");
            cb.endText();

            cb.beginText();
            cb.setFontAndSize(bf,6);
            cb.setTextMatrix(360,422);  //220
            cb.showText("TAXABLE\n" + "VALUE");
            cb.endText();

            cb.beginText();
            cb.setFontAndSize(bf,6);
            cb.setTextMatrix(362,395);  //220
            cb.showText(productPrice.toString());
            cb.endText();

            cb.beginText();
            cb.setFontAndSize(bf,6);
            cb.setTextMatrix(440,422);  //220
            cb.showText("IGST\n" + "(Value | %)");
            cb.endText();

            cb.beginText();
            cb.setFontAndSize(bf,6);
            cb.setTextMatrix(440,395);  //220
            cb.showText("0.0|0.0");
            cb.endText();

            cb.beginText();
            cb.setFontAndSize(bf,6);
            cb.setTextMatrix(500,422);  //220
            cb.showText("TOTAL\n" + "(Including GST)");
            cb.endText();

            cb.beginText();
            cb.setFontAndSize(bf,6);
            cb.setTextMatrix(500,395);  //220
            cb.showText(productPrice.toString());
            cb.endText();

            cb.beginText();
            cb.setFontAndSize(bf,12);
            cb.setTextMatrix(290,355);  //220
            cb.showText("NET TOTAL (In Value)");
            cb.endText();

            cb.beginText();
            cb.setFontAndSize(bf,10);
            cb.setTextMatrix(500,355);  //220
            cb.showText(productPrice.toString());
            cb.endText();



            Font fontFormat=new Font();
            fontFormat.setSize(12);
            fontFormat.setFamily("Arial");
            fontFormat.setStyle(Font.BOLD);

            Font normalfont=new Font();
            normalfont.setSize(10);
            normalfont.setFamily("Arial");
            normalfont.setStyle(Font.NORMAL);

            PdfPTable table = new PdfPTable(3);
            table.setTotalWidth(520);



            PdfPCell sku = new PdfPCell();
            Paragraph p = new Paragraph("SHIPPING ADDRESS:",fontFormat);
            p.setAlignment(Element.ALIGN_MIDDLE);
            sku.addElement(p);


            PdfPCell item = new PdfPCell();
            Paragraph pitem = new Paragraph("SOLD BY:",fontFormat);
            pitem.setAlignment(Element.ALIGN_MIDDLE);
            item.addElement(pitem);

            PdfPCell qty = new PdfPCell();
            Paragraph qtyitem = new Paragraph("INVOICE DETAILS:",fontFormat);
            pitem.setAlignment(Element.ALIGN_MIDDLE);
            qty.addElement(qtyitem);




            StringBuffer consigneeDetails = new StringBuffer();
            String addrss = saleOrder.getConsigneeAddress() != null ? saleOrder.getConsigneeAddress().replaceAll("\n", " ") : "";
            addrss = addrss.length() > 130 ? addrss.substring(0 , 129) : addrss;
            consigneeDetails.append(saleOrder.getConsigneeName() != null ? saleOrder.getConsigneeName()+"\n" : "");
            consigneeDetails.append(addrss+"\n");
            consigneeDetails.append(saleOrder.getConsigneeCity() != null ? saleOrder.getConsigneeCity()+" " :"");
            consigneeDetails.append(saleOrder.getConsigneePinCode() != null ? saleOrder.getConsigneePinCode()+"\n" : "");
            consigneeDetails.append(saleOrder.getConsigneeState() != null ? saleOrder.getConsigneeState()+"\n" : "");
            consigneeDetails.append(saleOrder.getConsigneeCountry() != null ? saleOrder.getConsigneeCountry()+"\n" : "");
            consigneeDetails.append("Ph: ");
            consigneeDetails.append(saleOrder.getConsigneeMobileNumber() != null ? saleOrder.getConsigneeMobileNumber()+"\n" : "");

            StringBuffer senderDetails = new StringBuffer();
            senderDetails.append(saleOrder.getSenderName() != null ? saleOrder.getSenderName()+"\n" : "");
            addrss = saleOrder.getSenderAddress() != null ? saleOrder.getSenderAddress().replaceAll("\n", "") : "";
            addrss = addrss.length() > 130 ? addrss.substring(0 , 129) : addrss;
            senderDetails.append(addrss+"\n");
            senderDetails.append(saleOrder.getSenderCity() != null ? saleOrder.getSenderCity()+" " :"");
            senderDetails.append(saleOrder.getSenderPinCode() != null ? saleOrder.getSenderPinCode()+"\n" : "");
            senderDetails.append(saleOrder.getSenderState() != null ? saleOrder.getSenderState()+"\n" : "");
            senderDetails.append(saleOrder.getSenderCountry() != null ? saleOrder.getSenderCountry()+"\n" : "");
            senderDetails.append("Ph: ");
            senderDetails.append(saleOrder.getSenderMobileNumber() != null ? saleOrder.getSenderMobileNumber()+"\n" : "");
            senderDetails.append("Website: https://www.couriercart.in\n");
            senderDetails.append("Email: ");
            senderDetails.append(saleOrder.getSenderEmail() != null ? saleOrder.getSenderEmail()+"\n" : "");
            senderDetails.append(" \n");

            StringBuffer invoiceDetails = new StringBuffer();
            String orderDate = SharedMethords.getOnlyDate(saleOrder.getOrderDate());

            invoiceDetails.append("INVOICE NO.         : ");
            invoiceDetails.append(saleOrder.getReferanceNo() != null ? saleOrder.getReferanceNo()+"\n" : ""+"\n");
            invoiceDetails.append("INVOICE DATE       : ");
            invoiceDetails.append(orderDate != null ? orderDate+"\n" : ""+"\n");
            invoiceDetails.append("ORDER NO.           : ");
            invoiceDetails.append(saleOrder.getClientOrderId() != null ? saleOrder.getClientOrderId()+"\n" :""+"\n");
            invoiceDetails.append("ORDER DATE         : ");
            invoiceDetails.append(orderDate != null ? orderDate+"\n" :""+"\n");
            invoiceDetails.append("SHIPPED BY          : ");
            invoiceDetails.append(saleOrder.getCourierCode() != null ? saleOrder.getCourierCode()+"\n" : ""+"\n");
            invoiceDetails.append("AWB NO.                : ");
            invoiceDetails.append(saleOrder.getCourierAWBNumber() != null ? saleOrder.getCourierAWBNumber()+"\n" : ""+"\n");
            invoiceDetails.append("PAYMENT\n" + "TYPE               : ");
            invoiceDetails.append(saleOrder.getPaymentType() != null ? saleOrder.getPaymentType()+"\n" : ""+"\n");

            PdfPCell consigneee = new PdfPCell();
            Paragraph consigneeeitem = new Paragraph(consigneeDetails.toString(),normalfont);
            pitem.setAlignment(Element.ALIGN_LEFT);
            consigneee.addElement(consigneeeitem);

            PdfPCell sender = new PdfPCell();
            Paragraph senderitem = new Paragraph(senderDetails.toString(),normalfont);
            pitem.setAlignment(Element.ALIGN_RIGHT);
            sender.addElement(senderitem);

            PdfPCell invoice = new PdfPCell();
            Paragraph invoiceitem = new Paragraph(invoiceDetails.toString(),normalfont);
            pitem.setAlignment(Element.ALIGN_RIGHT);
            invoice.addElement(invoiceitem);

            table.addCell(sku);
            table.addCell(item);
            table.addCell(qty);
            table.addCell(consigneee);

            table.addCell(sender);
            table.addCell(invoice);
            table.writeSelectedRows(
                    0, -1, two.getLeft(35), two.getTop(170), writer.getDirectContent());

            // Signatur Ractangle
            PdfPTable rectangleTable = new PdfPTable(1);
            rectangleTable.setTotalWidth(150);
            PdfPCell signatureBox = new PdfPCell();
            Paragraph pr = new Paragraph("                  \n ");
            signatureBox.addElement(pr);
            rectangleTable.addCell(signatureBox);
            rectangleTable.writeSelectedRows(
                    0, -1, two.getLeft(35), two.getTop(504), writer.getDirectContent());

            cb.beginText();
            cb.setFontAndSize (nf, 8);
            cb.setTextMatrix(36,290);
            String clientCode = saleOrder.getClientCode();
            cb.showText("Authorized Signature for "+clientCode);
            cb.endText();

        } catch (Exception ex) {
            ex.printStackTrace();

        }


    }


    private void courierCartManifestPrint(Document document,PdfWriter writer, ThreeplManifest threeplManifest, Courier courier) throws Exception{
            PdfContentByte cb = writer.getDirectContent();
            BaseFont bf = FontFactory.getFont("Arial Black", 7, Font.BOLD).getCalculatedBaseFont(false);
            BaseFont nf = FontFactory.getFont("Arial", 7, Font.NORMAL).getCalculatedBaseFont(false);
            Image image128 = null;
            Barcode128 code128 = null;
            Image image = null;
            ColumnText ct = null;
            Rectangle two = new Rectangle(595.0F, 842.0F);
            document.setPageSize(two);
            document.newPage();
            cb = writer.getDirectContent();

            Font fontFormat=new Font();
            fontFormat.setSize(10);
            fontFormat.setFamily("Arial");
            fontFormat.setStyle(Font.BOLD);

            Font normalfont=new Font();
            normalfont.setSize(10);
            normalfont.setFamily("Arial");
            normalfont.setStyle(Font.NORMAL);

            PdfPTable Table1 = new PdfPTable(2);
            Table1.setTotalWidth(520);

            PdfPCell signatureBox = new PdfPCell();
            Paragraph pr = new Paragraph("Manifest Number\n",fontFormat);
            pr.setAlignment(Element.ALIGN_MIDDLE);
            signatureBox.addElement(pr);

            String manifestNumber1= threeplManifest.getManifestNumber();
            code128 = new Barcode128();
            code128.setCode(manifestNumber1);
            image128 = code128.createImageWithBarcode(cb, null, null);
            image128.scaleAbsolute(120f,30f);
            //image128.setAbsolutePosition(300f,750f);//320
            //document.add((image128));

            PdfPCell signatureBox2 = new PdfPCell();
            image128.setAlignment(Element.ALIGN_MIDDLE);
            signatureBox2.addElement(image128);

            Table1.addCell(signatureBox);
            Table1.addCell(signatureBox2);

            Table1.writeSelectedRows(
                    0, -1, two.getLeft(35), two.getTop(40), writer.getDirectContent());

        PdfPTable Table2 = new PdfPTable(8);
        Table2.setTotalWidth(520);

        PdfPCell serialNumber = new PdfPCell();
        Paragraph serialNumberItem = new Paragraph("S.NO.",fontFormat);
        serialNumberItem.setAlignment(Element.ALIGN_MIDDLE);
        serialNumberItem.setAlignment(Element.ALIGN_MIDDLE);
        serialNumber.addElement(serialNumberItem);

        PdfPCell awbNumber = new PdfPCell();
        Paragraph awbNumberItem = new Paragraph("CCT AWB",fontFormat);
        awbNumberItem.setAlignment(Element.ALIGN_MIDDLE);
        awbNumber.addElement(awbNumberItem);

        PdfPCell courierName = new PdfPCell();
        Paragraph courierNameItem = new Paragraph("Courier Name",fontFormat);
        courierNameItem.setAlignment(Element.ALIGN_MIDDLE);
        courierName.addElement(courierNameItem);

        PdfPCell courierAwb = new PdfPCell();
        Paragraph courierAwbItem = new Paragraph("Courier AWB",fontFormat);
        courierAwbItem.setAlignment(Element.ALIGN_MIDDLE);
        courierAwb.addElement(courierAwbItem);

        PdfPCell consignee = new PdfPCell();
        Paragraph consigneeName = new Paragraph("Consignee Name",fontFormat);
        consigneeName.setAlignment(Element.ALIGN_MIDDLE);
        consignee.addElement(consigneeName);

        PdfPCell product = new PdfPCell();
        Paragraph productItem = new Paragraph("Product",fontFormat);
        productItem.setAlignment(Element.ALIGN_MIDDLE);
        product.addElement(productItem);

        PdfPCell productQTY = new PdfPCell();
        Paragraph productQTYItem = new Paragraph("Product QTY",fontFormat);
        productQTYItem.setAlignment(Element.ALIGN_MIDDLE);
        productQTY.addElement(productQTYItem);

        PdfPCell client = new PdfPCell();
        Paragraph clientOrderId = new Paragraph("Client Order Id",fontFormat);
        clientOrderId.setAlignment(Element.ALIGN_MIDDLE);
        client.addElement(clientOrderId);

        Table2.addCell(serialNumber);
        Table2.addCell(awbNumber);
        Table2.addCell(courierName);
        Table2.addCell(courierAwb);
        Table2.addCell(consignee);
        Table2.addCell(product);
        Table2.addCell(productQTY);
        Table2.addCell(client);

        String awbNumvers = threeplManifest.getAwbNumbers();
        List<String> referanceNos = Arrays.asList(awbNumvers.split(","));
        List<SaleOrder>  awbNum= saleOrderRepository.findByReferanceNoIn(referanceNos);
        Integer sr=0;
        float tabelHight = Table2.getRowHeight(0);
        for(SaleOrder saleOrder:awbNum){
             sr +=1;
            if(saleOrder!=null){
                PdfPCell snNumber = new PdfPCell(new Paragraph(sr.toString()));
                PdfPCell referanceNo = new PdfPCell(new Paragraph(saleOrder.getReferanceNo().toString()));
                PdfPCell courierCode = new PdfPCell(new Paragraph(saleOrder.getCourierCode().toString()));
                PdfPCell courierAwbNum = new PdfPCell(new Paragraph(saleOrder.getCourierAWBNumber().toString()));
                PdfPCell consigneeNam = new PdfPCell(new Paragraph(saleOrder.getConsigneeName().toString()));
                PdfPCell productName = new PdfPCell(new Paragraph(saleOrder.getProductName().toString()));
                PdfPCell productQty = new PdfPCell(new Paragraph(saleOrder.getProductQuantity().toString()));
                PdfPCell clientOrder = new PdfPCell(new Paragraph(saleOrder.getClientOrderId().toString()));

                Table2.addCell(snNumber);
                Table2.addCell(referanceNo);
                Table2.addCell(courierCode);
                Table2.addCell(courierAwbNum);
                Table2.addCell(consigneeNam);
                Table2.addCell(productName);
                Table2.addCell(productQty);
                Table2.addCell(clientOrder);

                tabelHight = tabelHight + Table2.getRowHeight(sr);
            }
            continue;
        }

        Table2.writeSelectedRows(
                0, -1, two.getLeft(35), two.getTop(90), writer.getDirectContent());

            //FE Table
            PdfPTable Table3 = new PdfPTable(4);
            Table3.setTotalWidth(520);

            PdfPCell sku = new PdfPCell();
            Paragraph p = new Paragraph("Pickup Date & Time",fontFormat);
            p.setAlignment(Element.ALIGN_MIDDLE);
            p.setAlignment(Element.ALIGN_MIDDLE);
            sku.addElement(p);

            PdfPCell item = new PdfPCell();
            Paragraph pitem = new Paragraph("FE Name",fontFormat);
            pitem.setAlignment(Element.ALIGN_MIDDLE);
            item.addElement(pitem);

            PdfPCell qty = new PdfPCell();
            Paragraph qtyitem = new Paragraph("FE Phone",fontFormat);
            pitem.setAlignment(Element.ALIGN_MIDDLE);
            qty.addElement(qtyitem);


            PdfPCell amount = new PdfPCell();
            Paragraph amountitem = new Paragraph("FE Signature",fontFormat);
            pitem.setAlignment(Element.ALIGN_MIDDLE);
            amount.addElement(amountitem);

            PdfPCell blankOne = new PdfPCell(new Paragraph("                  \n "));
            PdfPCell blankTwo = new PdfPCell(new Paragraph(" "));
            PdfPCell blank3 = new PdfPCell(new Paragraph(" "));
            PdfPCell blank4 = new PdfPCell(new Paragraph(" "));

            Table3.addCell(sku);
            Table3.addCell(item);
            Table3.addCell(qty);
            Table3.addCell(amount);

            Table3.addCell(blankOne);
            Table3.addCell(blankTwo);
            Table3.addCell(blank3);
            Table3.addCell(blank4);
            Table3.writeSelectedRows(
                    0, -1, two.getLeft(35), two.getTop(110+tabelHight), writer.getDirectContent());


    }

    private ByteArrayOutputStream convertPDFToByteArray(String fileName) {

        InputStream inputStream = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            inputStream = new FileInputStream(fileName);
            byte[] buffer = new byte[1024];
            baos = new ByteArrayOutputStream();
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ee) {
                    ee.printStackTrace();
                }
            }
        }
        return baos;
    }
}
