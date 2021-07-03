package com.weblearnex.app.config;

import com.weblearnex.app.entity.master.BulkMaster;
import com.weblearnex.app.entity.setup.ClientFacility;

public interface MessageProperty {

    public static final String RECORD_GET_SUCCESS_MSG = "record.get.success.msg";
    public static final String RECORD_GET_ERROR_MSG = "record.get.error.msg";
    public static final String EXCEPTION_MSG = "exception.msg";

    public static final String RECORD_NOT_FOUND = "record.not.found";
    public static final String RECORD_ADDED_MSG = "record.added.msg";
    public static final String RECORD_UPDATED_MSG = "record.updated.msg";
    public static final String RECORD_DELETE_MSG = "record.delete.msg";
    public static final String ERROR_AT_SAVE_MSG = "error.at.save.msg";
    public static final String ERROR_AT_UPDATE_MSG = "error.at.update.msg";

    public static final String STATUS_ADDED_MSG = "status.add.msg";
    public static final String STATUS_ADDED_ERROR_MSG = "status.add.error.msg";
    public static final String STATUS_UPDATED_MSG = "status.update.msg";
    public static final String STATUS_UPDATED_ERROR_MSG = "status.update.error.msg";
    public static final String STATUS_DELETE_MSG = "status.delete.msg";
    public static final String STATUS_ALREADY_EXIST_MSG = "status.code.already.exist.msg";
    public static final String STATUS_ID_NOT_FOUND = "status.id.not.exist";
    public static final String STATUS_ID_FOUND = "status.id.exist";
    public static final String STATUS_NAME_ALREADY_EXIST_MSG = "status.name.already.exist.msg";

    public static final String ST_ADDED_MSG = "st.add.msg";
    public static final String ST_ADDED_ERROR_MSG = "st.add.error.msg";
    public static final String ST_UPDATED_MSG = "st.update.msg";
    public static final String ST_UPDATED_ERROR_MSG = "st.update.error.msg";
    public static final String ST_DELETE_MSG = "st.delete.msg";
    public static final String ST_ALREADY_EXIST_MSG = "st.already.exist.msg";
    public static final String ST_CODE_DOES_NOT_NULL_OR_BLANK = "st.code.does.not.null.blank";



    public static final String CLIENT_ADDED_MSG = "client.add.msg";
    public static final String CLIENT_ADDED_ERROR_MSG = "client.add.error.msg";
    public static final String CLIENT_UPDATED_MSG = "client.update.msg";
    public static final String CLIENT_UPDATED_ERROR_MSG = "client.update.error.msg";
    public static final String CLIENT_DELETE_MSG = "client.delete.msg";
    public static final String CLIENT_ALREADY_EXIST_MSG = "client.already.exist.msg";
    public static final String CLIENT_ID_NOT_FOUND = "client.id.not.exist";
    public static final String CLIENT_Name_EXIST_MSG = "client.name.already.exist.msg";

    public static final String COUNTRY_ADDED_MSG = "country.add.msg";
    public static final String COUNTRY_ADDED_ERROR_MSG = "country.add.error.msg";
    public static final String COUNTRY_UPDATED_MSG = "country.update.msg";
    public static final String COUNTRY_UPDATED_ERROR_MSG = "country.update.error.msg";
    public static final String COUNTRY_DELETE_MSG = "country.delete.msg";
    public static final String COUNTRY_CODE_ALREADY_EXIST_MSG = "country.code.already.exist.msg";
    public static final String COUNTRY_ID_NOT_FOUND = "country.id.not.exist";
    public static final String COUNTRY_CODE_NOT_FOUND = "country.code.not.exist";
    public static final String COUNTRY_NAME_ALREADY_EXIST_MSG = "country.name.already.exist.msg";
    public static final String COUNTRY_CODE_DOES_NOT_NULL_OR_BLANK = "country.code.does.not.null.blank";
    public static final String COUNTRY_ID_DOES_NOT_EMPTY_OR_NO_DATA_FOUND_IN_DATABASE = "country.id.does.not.null.or.id.does.not.found.in.database";
    public static final String COUNTRY_NAME_NOT_EXIST_MSG = "country.name.not.exist.msg";
    public static final String COUNTRY_NAME_DOES_NOT_NULL_OR_BLANK = "country.name.does.not.null.blank";

    public static final String STATE_ADDED_MSG = "state.add.msg";
    public static final String STATE_ADDED_ERROR_MSG = "state.add.error.msg";
    public static final String STATE_UPDATED_MSG = "state.update.msg";
    public static final String STATE_UPDATED_ERROR_MSG = "state.update.error.msg";
    public static final String STATE_DELETE_MSG = "state.delete.msg";
    public static final String STATE_ALREADY_EXIST_MSG = "state.code.already.exist.msg";
    public static final String STATE_ID_NOT_FOUND = "state.id.not.exist";
    public static final String STATE_NAME_ALREADY_EXIST_MSG = "state.name.already.exist.msg";
    public static final String STATE_CODE_DOES_NOT_NULL_OR_BLANK = "state.code.does.not.null.blank";
    public static final String STATE_CODE_NOT_EXIST_MSG = "state.code.not.exist.msg";
    public static final String STATE_ID_DOES_NOT_EMPTY_OR_NO_DATA_FOUND_IN_DATABASE = "state.id.does.not.null.or.id.does.not.found.in.database";
    public static final String STATE_NAME_NOT_EXIST_MSG = "state.name.not.exist.msg";
    public static final String STATE_NAME_DOES_NOT_NULL_OR_BLANK = "state.name.does.not.null.blank";

    public static final String CITY_ADDED_MSG = "city.add.msg";
    public static final String CITY_ADDED_ERROR_MSG = "city.add.error.msg";
    public static final String CITY_UPDATED_MSG = "city.update.msg";
    public static final String CITY_UPDATED_ERROR_MSG = "city.update.error.msg";
    public static final String CITY_DELETE_MSG = "city.delete.msg";
    public static final String CITY_ALREADY_EXIST_MSG = "city.code.already.exist.msg";
    public static final String CITY_ID_NOT_FOUND = "city.id.not.exist";
    public static final String CITY_NAME_ALREADY_EXIST_MSG = "city.name.already.exist.msg";
    public static final String CITY_CODE_DOES_NOT_NULL_OR_BLANK = "city.code.does.not.null.blank";
    public static final String CITY_NAME_DOES_NOT_NULL_OR_BLANK = "city.name.does.not.null.blank";
    public static final String CITY_NAME_NOT_EXIST_MSG = "city.name.not.exist.msg";
    public static final String CITY_CODE_NOT_EXIST_MSG = "city.code.not.exist.msg";


    public static final String AWBSERIES_ALREADY_EXIST_MSG = "awbseries.code.already.exist.msg";
    public static final String AWBSERIES_ID_DOES_NOT_EMPTY_OR_NO_DATA_FOUND_IN_DATABASE ="awbseries.id.does.not.empty.or.no.data.found.in.database" ;
    public static final String AWBSERIES_ADDED_MSG = "awbseries.add.msg";
    public static final String AWBSERIES_ADDED_ERROR_MSG = "awbseries.add.error.msg";
    public static final String AWBSERIES_UPDATED_MSG = "awbseries.update.msg";
    public static final String AWBSERIES_UPDATED_ERROR_MSG = "awbseries.update.error.msg";
    public static final String AWBSERIES_DELETE_MSG = "awbseries.delete.msg";
    public static final String AWBSERIES_ID_NOT_FOUND = "awbseries.id.not.exist";
    public static final String AWBSERIES_CODE_DOES_NOT_NULL_OR_BLANK = "awbseries.code.does.not.null.blank";

    public static final String DOMASTIC_RATE_CARD_NULL_MSG = "domastic.rate.card.null.msg";
    public static final String DOMASTIC_RATE_CARD_ADD_SUCCESS_MSG = "domastic.rate.card.add.succcess.msg";
    public static final String DOMASTIC_RATE_CARD_ERROR_MSG = "domastic.rate.card.error.msg";
    public static final String DOMASTIC_RATE_CARD_CODE_ALREADY_EXIST = "domastic.rate.card.code.already.exist";
    public static final String DOMASTIC_RATE_CARD_CODE_EMPTY_MSG = "domastic.rate.card.code.empty.msg";
    public static final String DOMASTIC_RATE_CARD_FREIGHT_TYPE_NULL_MSG = "domastic.rate.card.code.freight.type.null.msg";
    public static final String DOMASTIC_RATE_CARD_MATRIX_EMPTY_MSG = "domastic.rate.card.matrix.empty.code";
    public static final String DOMASTIC_RATE_CARD_ID_INVALIDE = "domastic.rate.card.id.invalid";


    public static final String PINCODE_ADDED_MSG = "pincode.add.msg";
    public static final String PINCODE_ADDED_ERROR_MSG = "pincode.add.error.msg";
    public static final String PINCODE_UPDATED_MSG = "pincode.update.msg";
    public static final String PINCODE_UPDATED_ERROR_MSG = "pincode.update.error.msg";
    public static final String PINCODE_DELETE_MSG = "pincode.delete.msg";
    public static final String PINCODE_DELETE_ERROR_MSG = "pincode.delete.error.msg";
    public static final String PINCODE_ALREADY_EXIST_MSG = "pincode.already.exist.msg";
    public static final String PINCODE_ID_NOT_FOUND = "pincode.id.not.exist";
    public static final String PINCODE_DOES_NOT_NULL_OR_BLANK = "pincode.does.not.null.blank";
    public static final String PINCODE_ID_DOES_NOT_NULL_OR_BLANK = "pincode.id.does.not.null.blank";
    public static final String PINCODE_ID_FOUND = "pincode.id.exist";
    public static final String PINCODE_VALUE_NOT_FOUND = "pincode.value.not.exist";
    public static final String PINCODE_VALUE_FOUND = "pincode.value.exist";


    public static final String COURIER_NMAE_DOES_NOT_NULL_OR_BLANK="courier.name.does.not.null.blank";
    public static final String COURIER_NAME_ALREADY_EXIST="courier.name.already.exist.msg";
    public static final String COURIER_CODE_ALREADY_EXIST="courier.code.already.exist.msg";
    public static final String COURIER_CODE_DOES_NOT_NULL_OR_BLANK = "courier.code.does.not.null.blank";
    public static final String COURIER_MOBILE_DOES_NOT_NULL_OR_BLANK = "courier.mobile.does.not.null.blank";
    public static final String COURIER_PIN_CODE_DOES_NOT_NULL_OR_BLANK = "courier.pin.code.does.not.null.blank";
    public static final String COURIER_ID_DOES_NOT_EMPTY_OR_NO_DATA_FOUND_IN_DATABASE = "courier.id.does.not.empty.or.no.data.found.in.database";
    public static final String COURIER_ADDED_MSG = "courier.add.msg";
    public static final String COURIER_ADDED_ERROR_MSG = "courier.add.error.msg";
    public static final String COURIER_ID_NOT_FOUND = "courier.id.not.exist";
    public static final String COURIER_ID_FOUND = "courier.id.exist";
    public static final String COURIER_UPDATED_MSG = "courier.update.msg";
    public static final String COURIER_UPDATED_ERROR_MSG = "courier.update.error.msg";
    public static final String COURIER_DELETE_MSG = "courier.delete.msg";

    public static final String DISPLAY_NAME_ALREADY_EXIST_MSG = "display.Name.already.exist.msg";
    public static final String BULKHEADER_ALREADY_EXIST_MSG = "bulkHeader.already.exist.msg";
    public static final String BULKHEADER_ADDED_MSG = "bulkHeader.add.msg";
    public static final String BULKHEADER_ADDED_ERROR_MSG = "bulkHeader.add.error.msg";
    public static final String BULKHEADER_ID_DOES_NOT_EMPTY_OR_NO_DATA_FOUND_IN_DATABASE = "bulkHeader.id.does.not.null.or.id.does.not.found.in.database";
    public static final String BULKHEADER_UPDATED_MSG = "bulkHeader.update.msg";
    public static final String BULKHEADER_UPDATED_ERROR_MSG = "bulkHeader.update.error.msg";
    public static final String BULKHEADER_ID_NOT_FOUND = "bulkHeader.id.not.exist";
    public static final String BULKHEADER_DELETE_MSG = "bulkHeader.delete.msg";
    public static final String BULKHEADER_ID_FOUND = "bulkHeader.id.exist";


    public static final String BULKMASTER_ALREADY_EXIST_MSG = "bulkMaster.already.exist.msg";
    public static final String BULKMASTER_ADDED_MSG = "bulkMaster.add.msg";
    public static final String BULKMASTER_ADDED_ERROR_MSG = "bulkMaster.add.error.msg";
    public static final String BULKMASTER_ID_DOES_NOT_EMPTY_OR_NO_DATA_FOUND_IN_DATABASE = "bulkMaster.id.does.not.null.or.id.does.not.found.in.database";
    public static final String BULKMASTER_UPDATED_MSG = "bulkMaster.update.msg";
    public static final String BULKMASTER_UPDATED_ERROR_MSG = "bulkMaster.update.error.msg";
    public static final String BULKMASTER_ID_NOT_FOUND = "bulkMaster.id.not.exist";
    public static final String BULKMASTER_ID_FOUND = "bulkMaster.id.exist";
    public static final String BULKMASTER_DELETE_MSG = "bulkMaster.delete.msg";


    public static final String STATUSFLOW_ALREADY_EXIST_MSG = "statusflow.code.already.exist.msg";
    public static final String STATUSFLOW_ADDED_MSG = "statusflow.add.msg";
    public static final String STATUSFLOW_ADDED_ERROR_MSG = "statusflow.add.error.msg";
    public static final String STATUSFLOW_UPDATED_MSG = "statusflow.update.msg";
    public static final String STATUSFLOW_UPDATED_ERROR_MSG = "statusflow.update.error.msg";
    public static final String STATUSFLOW_ID_NOT_FOUND = "statusflow.id.not.exist";
    public static final String STATUSFLOW_ID_FOUND = "statusflow.id.exist";
    public static final String STATUSFLOW_DELETE_MSG = "statusflow.delete.msg";
    public static final String STATUSFLOW_NAME_ALREADY_EXIST_MSG = "statusflow.name.already.exist.msg";

    public static final String ROLE_NAME_ALREADY_EXIST_MSG = "role.name.already.exist.msg";
    public static final String ROLE_PAGEIDS_ALREADY_EXIST_MSG = "role.pageids.already.exist.msg";
    public static final String ROLE_ADDED_MSG = "role.add.msg";
    public static final String ROLE_ADDED_ERROR_MSG = "role.add.error.msg";
    public static final String ROLE_NAME_DOES_NOT_NULL_OR_BLANK="role.name.does.not.null.blank";
    public static final String ROLE_PAGEIDS_DOES_NOT_NULL_OR_BLANK="role.id.not.exist";
    public static final String ROLE_ID_NOT_FOUND = "role.id.not.exist";
    public static final String ROLE_UPDATED_ERROR_MSG = "role.update.error.msg";
    public static final String ROLE_DELETE_MSG = "role.delete.msg";
    public static final String ROLE_ALREADY_EXIST_MSG = "role.code.already.exist.msg";
    public static final String ROLE_UPDATED_MSG = "role.update.msg";
    public static final String ROLE_ID_FOUND="role.id.exist";


    public static final String CONFIGRATION_TYPE_DOES_NOT_NULL_OR_BLANK="configration.type.does.not.null.blank";
    public static final String CONFIGRATION_TYPE_ALREADY_EXIST_MSG="configration.type.already.exist.msg";
    public static final String CONFIGRATION_CODE_ALREADY_EXIST_MSG="configration.code.already.exist.msg";
    public static final String CONFIGRATION_VALUE_ALREADY_EXIST_MSG="configration.value.already.exist.msg";
    public static final String CONFIGRATION_ADDED_MSG = "configration.add.msg";
    public static final String CONFIGRATION_ADDED_ERROR_MSG = "configration.add.error.msg";
    public static final String CONFIGRATION_CODE_DOES_NOT_EMPTY_OR_NO_DATA_FOUND_IN_DATABASE="configration.code.does.not.empty.or.no.data.found.in.database";
    public static final String CONFIGRATION_UPDATED_MSG = "configration.update.msg";
    public static final String CONFIGRATION_UPDATED_ERROR_MSG = "configration.update.error.msg";
    public static final String CONFIGRATION_ID_NOT_FOUND = "configration.id.not.exist";
    public static final String CONFIGRATION_DELETE_MSG = "configration.delete.msg";
    public static final String CONFIGRATION_ID_FOUND= "configration.id.exist";

    public static final String PAGE_MODULE_DOES_NOT_NULL_OR_BLANK = "page.module does.not.null.blank";
    public static final String PAGE_SUBMODULE_DOES_NOT_NULL_OR_BLANK = "page.submodule.does.not.null.blank";
    public static final String PAGE_DISPLAY_NAME_DOES_NOT_NULL_OR_BLANK = "page.display.name does.not.null.blank";
    public static final String PAGE_URL_DOES_NOT_NULL_OR_BLANK = "page.url.does.not.null.blank";
    public static final String PAGE_DISCRIPTION_DOES_NOT_NULL_OR_BLANK = "page.discription.does.not.null.blank";
    public static final String PAGE_ADDED_MSG = "page.add.msg";
    public static final String PAGE_ADDED_ERROR_MSG = "page.add.error.msg";
    public static final String PAGE_ID_NOT_FOUND = "page.id.not.exist";
    public static final String PAGE_UPDATED_MSG = "page.update.msg";
    public static final String PAGE_UPDATED_ERROR_MSG = "Page.update.error.msg";
    public static final String PAGE_DELETE_MSG = "page.delete.msg";
    public static final String PAGE_ID_FOUND = "page.id.exist";

    public static final String BRANCH_ALREADY_EXIST_MSG = "branch.already.exist.msg";
    public static final String BRANCH_ADDED_MSG = "branch.add.msg";
    public static final String BRANCH_ADDED_ERROR_MSG = "branch.add.error.msg";
    public static final String BRANCH_ID_DOES_NOT_EMPTY_OR_NO_DATA_FOUND_IN_DATABASE = "branch.id.does.not.null.or.id.does.not.found.in.database";
    public static final String BRANCH_UPDATED_MSG = "branch.update.msg";
    public static final String BRANCH_UPDATED_ERROR_MSG = "branch.update.error.msg";
    public static final String BRANCH_ID_NOT_FOUND = "branch.id.not.exist";
    public static final String BRANCH_DELETE_MSG = "branch.delete.msg";
    public static final String BRANCH_ID_FOUND = "branch.id.exist";
    public static final String BRANCH_CODE_EXIST_MSG = "branch.code.already.exist.msg";

    public static final String COLOADER_NAME_DOES_NOT_NULL_OR_BLANK = "coloader.name.does.not.null.blank";
    public static final String COLOADER_CODE_DOES_NOT_NULL_OR_BLANK ="coloader.code.does.not.null.blank";
    public static final String COLOADER_REGISTER_ADD_DOES_NOT_NULL_OR_BLANK ="coloader.register.add.does.not.null.blank";
    public static final String COLOADER_PIN_CODE_DOES_NOT_NULL_OR_BLANK ="coloader.pin.code.does.not.null.blank";
    public static final String COLOADER_CONTACT_PERSON_DOES_NOT_NULL_OR_BLANK ="coloader.contact.person.code.does.not.null.blank";
    public static final String COLOADER_MOBILE_DOES_NOT_NULL_OR_BLANK ="coloader.mobile.does.not.null.blank";
    public static final String COLOADER_EMAIL_DOES_NOT_NULL_OR_BLANK ="coloader.email.does.not.null.blank";
    public static final String COLOADER_ID_NOT_FOUND = "coloader.id.not.exist";
    public static final String COLOADER_UPDATED_MSG = "coloader.update.msg";
    public static final String COLOADER_UPDATED_ERROR_MSG = "coloader.update.error.msg";
    public static final String COLOADER_DELETE_MSG = "coloader.delete.msg";
    public static final String COLOADER_ID_FOUND = "coloader.id.exist";
    public static final String COLOADER_ADDED_MSG = "coloader.add.msg";
    public static final String COLOADER_ADDED_ERROR_MSG = "coloader.add.error.msg";

    public static final String USER_PASWORD_DOES_NOT_NULL_OR_BLANK = "user.password.does.not.null.blank";
    public static final String USER_LOGIN_ID_DOES_NOT_NULL_OR_BLANK ="user.login.id.code.does.not.null.blank";
    public static final String USER_FIRST_NAME_DOES_NOT_NULL_OR_BLANK ="user.first.name.does.not.null.blank";
    public static final String USER_LAST_NAME_DOES_NOT_NULL_OR_BLANK ="user.last.name.does.not.null.blank";
    public static final String USER_CONTACT_DOES_NOT_NULL_OR_BLANK ="user.contact.does.not.null.blank";
    public static final String USER_GENDER_DOES_NOT_NULL_OR_BLANK ="user.mobile.does.not.null.blank";
    public static final String USER_PIN_CODE_DOES_NOT_NULL_OR_BLANK ="user.pin.code.does.not.null.blank";
    public static final String USER_ADDRESS_DOES_NOT_NULL_OR_BLANK ="user.address.does.not.null.blank";
    public static final String USER_PAN_NUMBER_DOES_NOT_NULL_OR_BLANK ="user.pan.number.does.not.null.blank";
    public static final String USER_AADHAR_NUMBER_DOES_NOT_NULL_OR_BLANK ="user.aadhar.number.does.not.null.blank";
    public static final String USER_STATE_DOES_NOT_NULL_OR_BLANK ="user.state.does.not.null.blank";
    public static final String USER_CITY_DOES_NOT_NULL_OR_BLANK ="user.city.does.not.null.blank";
    public static final String USER_COUNTRY_DOES_NOT_NULL_OR_BLANK ="user.country.does.not.null.blank";

    public static final String USER_TYPE_DOES_NOT_NULL_OR_BLANK ="user.type.does.not.null.blank";
    public static final String USER_ADDED_MSG = "user.add.msg";
    public static final String USER_UPDATED_MSG = "user.update.msg";
    public static final String USER_ADDED_ERROR_MSG = "user.add.error.msg";
    public static final String USER_DELETE_MSG = "user.delete.msg";
    public static final String USER_ID_NOT_FOUND = "user.id.not.exist";
    public static final String USER_ID_FOUND = "user.id.exist";


    public static final String CLIENT_FACILITY_ADDED_MSG = "client.facility.add.msg";
    public static final String CLIENT_FACILITY_ADDED_ERROR_MSG = "client.facility.add.error.msg";
    public static final String CLIENT_FACILITY_UPDATED_MSG = "client.facility.update.msg";
    public static final String CLIENT_FACILITY_DOES_NOT_NULL_OR_BLANK ="client.facility.does.not.null.blank";
    public static final String CLIENT_FACILITY_UPDATED_ERROR_MSG = "client.facility.update.error.msg";
    public static final String CLIENT_FACILITY_DELETE_MSG = "client.facility.delete.msg";
    public static final String CLIENT_FACILITY_ALREADY_EXIST_MSG = "client.facility.already.exist.msg";
    public static final String CLIENT_FACILITY_ID_NOT_FOUND = "client.facility.id.not.exist";
    public static final String CLIENT_FACILITY_Name_EXIST_MSG = "client.facility.name.already.exist.msg";

    public static final String SERVISABLE_PINCODE_ID_DOES_NOT_NULL_OR_BLANK = "servisable.pincode.id.does.not.null.blank";
    public static final String SERVISABLE_PINCODE_ID_NOT_FOUND = "servisable.pincode.id.not.exist";
    public static final String SERVISABLE_PINCODE_UPDATED_MSG = "servisable.pincode.update.msg";
    public static final String SERVISABLE_PINCODE_ADDED_ERROR_MSG = "servisable.pincode.add.error.msg";
    public static final String SERVISABLE_PINCODE_DELETE_MSG = "servisable.pincode.delete.msg";
    public static final String SERVISABLE_PINCODE_DELETE_ERROR_MSG = "servisable.pincode.delete.error.msg";
    public static final String SERVISABLE_PINCODE_ADDED_MSG = "servisable.pincode.add.msg";

    public static final String SERVICE_CODE_DOES_NOT_NULL_OR_BLANK = "Service.code.does.not.null.blank";
    public static final String SERVICE_NAME_DOES_NOT_NULL_OR_BLANK = "Service.name.does.not.null.blank";
    public static final String SERVICE_ADDED_MSG = "Service.add.msg";
    public static final String SERVICE_ADDED_ERROR_MSG = "Service.add.error.msg";
    public static final String SERVICE_ID_DOSE_NOT_NULL = "Service.id.dose.not.null";
    public static final String SERVICE_UPDATED_MSG = "Service.update.msg";
    public static final String SERVICE_UPDATED_ERROR_MSG = "Service.update.error.msg";
    public static final String SERVICE_ID_NOT_FOUND = "Service.id.not.exist";
    public static final String SERVICE_DELETE_MSG = "Service.delete.msg";
    public static final String SERVICE_ID_FOUND="Service.id.exist";


    public static final String SERVICE_PROVIDER_NAME_DOES_NOT_NULL_OR_BLANK = "Service.provider.name.does.not.null.blank";
    public static final String SERVICE_PROVIDER_CODE_DOES_NOT_NULL_OR_BLANK = "Service.provider.code.does.not.null.blank";
    public static final String SERVICE_PROVIDER_ADDED_MSG = "Service.provider.add.msg";
    public static final String SERVICE_PROVIDER_ADDED_ERROR_MSG = "Service.provider.add.error.msg";
    public static final String SERVICE_PROVIDER_ID_DOSE_NOT_NULL = "Service.provider.id.dose.not.null";
    public static final String SERVICE_PROVIDER_UPDATED_MSG = "Service.provider.update.msg";
    public static final String SERVICE_PROVIDER_UPDATED_ERROR_MSG = "Service.provider.update.error.msg";
    public static final String SERVICE_PROVIDER_ID_NOT_FOUND = "Service.provider.id.not.exist";
    public static final String SERVICE_PROVIDER_DELETE_MSG = "Service.provider.delete.msg";
    public static final String SERVICE_PROVIDER_ID_FOUND = "Service.provider.id.exist";


    public static final String AWB_NUMBER_DOES_NOT_EMPTY_OR_NO_DATA_FOUND_IN_DATABASE ="AwbNumber.does.not.empty.or.no.data.found.in.database" ;
    public static final String CLIENT_LENGTH_DOES_NOT_NULL_OR_BLANK="Client.length.dose.not.null.blank";
    public static final String CLIENT_WEIGHT_NOT_NULL_OR_BLANK="Client.weight.dose.not.null.blank";
    public static final String CLIENT_WIDTH_DOES_NOT_NULL_OR_BLANK="Client.width.dose.not.null.blank";
    public static final String CLIENT_HEIGTH_DOES_NOT_NULL_OR_BLANK="Client.height.dose.not.null.blank";
    public static final String LBH_ADDED_MSG = "LBH.add.msg";
    public static final String LBH_ADDED_ERROR_MSG = "LBH.add.error.msg";

    public static final String COURIER_PRIORITY_CODE_DOES_NOT_NULL_OR_BLANK = "Courier.priority.code.does.not.null.blank";
    public static final String COURIER_PRIORITY_ALREADY_EXIST="Courier.priority.already.exist.msg";
    public static final String COURIER_PRIORITY_NAME_DOES_NOT_NULL_OR_BLANK = "Courier.priority.name.code.does.not.null.blank";
    public static final String COURIER_PRIORITYS_DOES_NOT_NULL_OR_BLANK="Courier.prioritys.does.not.null.blank";
    public static final String COURIER_PRIORITY_ADDED_MSG = "Courier.priority.add.msg";
    public static final String COURIER_PRIORITY_ADDED_ERROR_MSG = "Courier.priority.add.error.msg";
    public static final String COURIER_PRIORITY_UPDATED_MSG = "Courier.priority.update.msg";
    public static final String COURIER_PRIORITY_UPDATED_ERROR_MSG = "Courier.priority.update.error.msg";
    public static final String COURIER_PRIORITY_ID_NOT_FOUND = "Courier.priority.id.not.exist";
    public static final String COURIER_PRIORITY_DELETE_MSG = "Courier.priority.delete.msg";
    public static final String COURIER_PRIORITY_ID_FOUND="Courier.priority.id.exist";
    public static final String PRIORITY_DOES_NOT_FOUND="Priority.dose.not.found";

    public static final String COURIER_STATUS_MAPPING_DOES_NOT_NULL_OR_BLANK="Courier.status.mapping.does.not.null.blank";
    public static final String COURIER_ID_ALREADY_EXIST="Courier.id.already.exist.msg";
    public static final String TOKEN_DOES_NOT_NULL_OR_BLANK="Token.id.does.not.null.blank";
    public static final String COURIER_STATUS_MAPPING_ADDED_MSG="Courier.status.mapping.add.msg";
    public static final String COURIER_STATUS_MAPPING_DOES_NOT_FOUND="Courier.status.mapping.dose.not.found";
    public static final String COURIER_STATUS_MAPPING_UPDATED_MSG="Courier.status.mapping.update.msg";
    public static final String COURIER_STATUS_MAPPING_UPDATED_ERROR_MSG="Courier.status.mapping.update.error.msg";
    public static final String COURIER_STATUS_MAPPING_ID_NOT_FOUND="Courier.status.mapping.id.not.exist";
    public static final String COURIER_STATUS_MAPPING_DELETE_MSG="Courier.status.mapping.delete.msg";
    public static final String COURIER_STATUS_MAPPING_ID_FOUND="Courier.status.mapping.id.exist";


    public static final String APICONFIG_TYPE_DOES_NOT_NULL_OR_BLANK="ApiConfig.type.does.not.null.blank";
    public static final String APIURL_DOES_NOT_NULL_OR_BLANK="ApiUrl.does.not.null.blank";
    public static final String ENTITY_TYPE_DOES_NOT_NULL_OR_BLANK="Entity.type.does.not.null.blank";
    public static final String REQUEST_METHOD_DOES_NOT_NULL_OR_BLANK="Request.method.does.not.null.blank";
    public static final String DATA_PARAMS_DOES_NOT_NULL_OR_BLANK="Data.params.does.not.null.blank";
    public static final String HEADER_PARAMS_DOES_NOT_NULL_OR_BLANK="Header.params.does.not.null.blank";
    public static final String RESPONSE_BEAN_DOES_NOT_NULL_OR_BLANK="Response.bean.does.not.null.blank";
    public static final String RESPONSE_TYPE_DOES_NOT_NULL_OR_BLANK="Response.type.does.not.null.blank";
    public static final String REQUEST_TYPE_DOES_NOT_NULL_OR_BLANK="Request.type.does.not.null.blank";
    public static final String API_TOKEN_DOES_NOT_NULL_OR_BLANK="Api.token.does.not.null.blank";
    public static final String CLIENT_ID_ALREADY_EXIST="Client.id.already.exist.msg";
    public static final String APICONFIG_ADDED_MSG="ApiConfig.add.msg";
    public static final String APICONFIG_ADDED_ERROR_MSG="ApiConfig.add.error.msg";
    public static final String APICONFIG_UPDATED_MSG="ApiConfig.update.msg";
    public static final String APICONFIG_UPDATED_ERROR_MSG="ApiConfig.update.error.msg";
    public static final String APICONFIG_ID_NOT_FOUND="ApiConfig.id.not.exist";
    public static final String APICONFIG_DELETE_MSG= "ApiConfig.delete.msg";
    public static final String APICONFIG_ID_FOUND="ApiConfig.id.exist";


    public static final String SALE_ORDER_REFERANCE_NO_ALREADY_EXIST_MSG = "sale.order.referance.no.already.exist.msg";
    public static final String SALE_ORDER_ADDED_MSG = "sale.order.add.msg";
    public static final String SALE_ORDER_ADDED_ERROR_MSG = "sale.order.add.error.msg";
    public static final String CLIENT_DELIVERY_ATTEMPT_EXIST_MSG = "client.develory_attempt.exist.msg";

    public static final String AWB_NUMBER_NOT_FOUND = "awb.number.not.exist";
    public static final String AWB_NUMBER_FOUND = "awb.number.exist";
    public static final String COURIER_CODE_NOT_FOUND = "courier.code.not.exist";
    public static final String COURIER_CODE_FOUND = "courier.code.exist";
    public static final String AWB_NUMBER_NOT_MORE_THAN_50 = "awb.number_not_more_than_50";
    public static final String COURIER_CODE_NOT_MORE_THAN_50 = "courier.code_not_more_than_50";
    public static final String COURIER_AWB_NUM_NOT_NOT_NULL  = "courier.awb.num_not_null.or.blank";

    public static final String CLIENT_ORDER_ID_NOT_NULL = "client.order.id.not.null";
    public static final String CLIENT_CODE_NOT_NULL = "client.code.not.null";
    public static final String AWB_NUMBER_NOT_NULL = "awb.Number.code.not.null";
    public static final String AMOUNT_NOT_NULL = "amount.code.not.null";
    public static final String DATE_NOT_NULL = "date.code.not.null";

    public static final String CLIENT_WAREHOUSE_NAME_DOES_NOT_NULL_OR_BLANK = "Client.warehouse.name.does.not.null.blank";
    public static final String CLIENT_WAREHOUSE_CODE_ALREADY_EXIST_MSG="Client.warehouse.code.already.exist.msg";
    public static final String CONTACT_NUMBER_DOES_NOT_NULL_OR_BLANK="Contact.number.does.not.null.blank";
    public static final String CONTACT_PERSON_NAME_DOES_NOT_NULL_OR_BLANK="Contact.person.name.does.not.null.blank";
    public static final String CLIENT_CODE_DOES_NOT_EMPTY_OR_NO_DATA_FOUND_IN_DATABASE="Client.code.does.not.empty.or.no.data.found.in.database";
    public static final String CLIENT_WAREHOUSE_ADDED_MSG = "Client.warehouse.add.msg";
    public static final String CLIENT_WAREHOUSE_ADDED_ERROR_MSG = "Client.warehouse.add.error.msg";
    public static final String CLIENT_WAREHOUSE_UPDATED_MSG = "Client.warehouse.update.msg";
    public static final String CLIENT_WAREHOUSE_UPDATED_ERROR_MSG = "Client.warehouse.update.error.msg";
    public static final String CLIENT_WAREHOUSE_ID_NOT_FOUND = "client.warehouse.id.not.exist";
    public static final String CLIENT_WAREHOUSE_DELETE_MSG = "client.warehouse.delete.msg";
    public static final String CLIENT_WAREHOUSE_ID_FOUND = "client.warehouse.id.exist";
    public static final String CLIENT_WAREHOUSE_STATE_DOES_NOT_NULL_OR_BLANK ="client.warehouse.state.does.not.null.blank";
    public static final String CLIENT_WAREHOUSE_CITY_DOES_NOT_NULL_OR_BLANK ="client.warehouse.city.does.not.null.blank";
    public static final String CLIENT_WAREHOUSE_COUNTRY_DOES_NOT_NULL_OR_BLANK ="client.warehouse.country.does.not.null.blank";


    public static final String CLIENT_COURIER_WAREHOUSE_MAPPING_CODE_DOES_NOT_EMPTY_OR_NO_DATA_FOUND_IN_DATABASE="Client.courier.warehouse.mapping.code.does.not.empty.or.no.data.found.in.database";
    public static final String CLIENT_COURIER_WAREHOUSE_MAPPING_CODE_ALREADY_EXIST_MSG="Client.courier.warehouse.mapping.code.already.exist.msg";
    public static final String CLIENT_COURIER_WAREHOUSE_MAPPING_SERVICE_PROVIDER_CODE_ALREADY_EXIST_MSG="Client.courier.warehouse.mapping.service.provider.code.already.exist.msg";
    public static final String CLIENT_COURIER_WAREHOUSE_MAPPING_SERVICE_PROVIDER_WAREHOUSE_CODE_ALREADY_EXIST_MSG="Client.courier.warehouse.mapping.service.provider.warehouse.code.already.exist.msg";
    public static final String CLIENT_COURIER_WAREHOUSE_MAPPING_ADDED_MSG = "Client.courier.warehouse.mapping.add.msg";
    public static final String CLIENT_COURIER_WAREHOUSE_MAPPING_ADDED_ERROR_MSG = "Client.courier.warehouse.mapping.add.error.msg";
    public static final String CLIENT_COURIER_WAREHOUSE_MAPPING_UPDATED_MSG = "Client.courier.warehouse.mapping.update.msg";
    public static final String CLIENT_COURIER_WAREHOUSE_MAPPING_UPDATED_ERROR_MSG = "Client.courier.warehouse.mapping.update.error.msg";
    public static final String CLIENT_COURIER_WAREHOUSE_MAPPING_ID_NOT_FOUND = "Client.courier.warehouse.mapping.id.not.exist";
    public static final String CLIENT_COURIER_WAREHOUSE_MAPPING_DELETE_MSG = "Client.courier.warehouse.mapping.delete.msg";
    public static final String CLIENT_COURIER_WAREHOUSE_MAPPING_ID_FOUND = "Client.courier.warehouse.mapping.id.exist";

    public static final String CLIENT_REMITTANCE_ID_NOT_FOUND = "client.remittance.id.not.exist";
    public static final String CLIENT_REMITTANCE_ACCOUNT_NUMBER_DOES_NOT_NULL_OR_BLANK = "client.remittance.AccountNo.does.not.null.blank";
    public static final String CLIENT_REMITTANCE_BANK_NAME_DOES_NOT_NULL_OR_BLANK = "client.remittance.BankName.does.not.null.blank";
    public static final String CLIENT_REMITTANCE_TRANSACTIONNo_DOES_NOT_NULL_OR_BLANK = "client.remittance.TransactionNo.does.not.null.blank";
    public static final String CLIENT_REMITTANCE_DEPOSITED_ATM_DOES_NOT_NULL_OR_BLANK = "client.remittance.DepositedAmt.does.not.null.blank";
    public static final String CLIENT_REMITTANCE_DEPOSIT_SLIP_DOES_NOT_NULL_OR_BLANK = "client.remittance.DepositSlip.does.not.null.blank";
    public static final String CLIENT_REMITTANCE_UPDATED_MSG = "client.remittance.update.msg";
    public static final String CLIENT_REMITTANCE_UPDATED_ERROR_MSG = "client.remittance.update.error.msg";

    public static final String COURIER_REMITTANCE_ID_NOT_FOUND = "courier.remittance.id.not.exist";
    public static final String COURIER_REMITTANCE_ACCOUNT_NUMBER_DOES_NOT_NULL_OR_BLANK = "courier.remittance.AccountNo.does.not.null.blank";
    public static final String COURIER_REMITTANCE_BANK_NAME_DOES_NOT_NULL_OR_BLANK = "courier.remittance.BankName.does.not.null.blank";
    public static final String COURIER_REMITTANCE_TRANSACTIONNo_DOES_NOT_NULL_OR_BLANK = "courier.remittance.TransactionNo.does.not.null.blank";
    public static final String COURIER_REMITTANCE_DEPOSITED_ATM_DOES_NOT_NULL_OR_BLANK = "courier.remittance.DepositedAmt.does.not.null.blank";
    public static final String COURIER_REMITTANCE_UPDATED_MSG = "courier.remittance.update.msg";
    public static final String COURIER_REMITTANCE_UPDATED_ERROR_MSG = "courier.remittance.update.error.msg";


    public static final String PENDING_VENDAR_STATUS_MAPPING_ID_NOT_FOUND="Pending.vendar.status.mapping.id.not.exist";
    public static final String PENDING_VENDAR_STATUS_MAPPING_DELETE_MSG="Pending.vendar.status.mapping.delete.msg";


    public static final String RATE_CARD_TYPE_NAME_SHOULD_NOT_NULL_OR_EMPTY = "type.Name.already.exist.msg";
    public static final String RATE_CARD_TYPE_CODE_ALREADY_EXIST_MSG = "type.code.already.exist.msg";
    public static final String RATE_CARD_TYPE_ADDED_MSG = "rate.card.type.add.msg";
    public static final String RATE_CARD_TYPE_ADDED_ERROR_MSG = "rate.card.type.add.error.msg";
    public static final String RATE_CARD_TYPE_ID_DOES_NOT_EMPTY_OR_NO_DATA_FOUND_IN_DATABASE = "rate.card.type.id.does.not.null.or.id.does.not.found.in.database";
    public static final String RATE_CARD_TYPE_UPDATED_MSG = "rate.card.type.update.msg";
    public static final String RATE_CARD_TYPE_UPDATED_ERROR_MSG = "rate.card.type.update.error.msg";
    public static final String RATE_CARD_TYPE_ID_NOT_FOUND = "rate.card.type.id.not.exist";
    public static final String RATE_CARD_TYPE_DELETE_MSG = "rate.card.type.delete.msg";
    public static final String RATE_CARD_TYPE_ID_FOUND = "rate.card.type.id.exist";
}

