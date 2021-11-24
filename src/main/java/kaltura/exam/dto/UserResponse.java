package kaltura.exam.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import kaltura.exam.service.UnixTimestampDeserializer;

import java.util.Date;

public class UserResponse {
    @JsonDeserialize(using = UnixTimestampDeserializer.class)
    public Date lastLoginDate;
}
