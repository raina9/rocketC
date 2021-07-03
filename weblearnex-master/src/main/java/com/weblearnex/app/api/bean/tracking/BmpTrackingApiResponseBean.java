package com.weblearnex.app.api.bean.tracking;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BmpTrackingApiResponseBean {
   private String status;
   private String message;
   private List<AwbDetails> response;

    @Data
    @NoArgsConstructor
   public static class AwbDetails{
        private String status;
        private String message;
        private String awbNumber;
        private List<PacketHistory> response;

        @Data
        @NoArgsConstructor
        public static class PacketHistory{
            private String statusCode;
            private String status;
            private String date;
            private String location;
            private String reasonCode;
            private String reason;
            private String remark;
        }
   }
}
