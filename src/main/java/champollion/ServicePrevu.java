package champollion;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServicePrevu {
	private int volumeCM;
    private int volumeTD;
    private int volumeTP;

    public ServicePrevu(int volumeCM, int volumeTD, int volumeTP) {
        this.volumeCM = volumeCM;
        this.volumeTD = volumeTD;
        this.volumeTP = volumeTP;
    }

}
