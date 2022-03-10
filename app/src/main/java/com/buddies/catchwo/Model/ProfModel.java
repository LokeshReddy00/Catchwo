package com.buddies.catchwo.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class ProfModel implements Parcelable {

    String imge,address,name,prof,phone;

    public ProfModel(String imge, String address, String name, String prof, String phone) {
        this.imge = imge;
        this.address = address;
        this.name = name;
        this.prof = prof;
        this.phone = phone;
    }

    public ProfModel() {
    }

    protected ProfModel(Parcel in) {
        imge = in.readString();
        address = in.readString();
        name = in.readString();
        prof = in.readString();
        phone = in.readString();
    }

    public static final Creator<ProfModel> CREATOR = new Creator<ProfModel>() {
        @Override
        public ProfModel createFromParcel(Parcel in) {
            return new ProfModel(in);
        }

        @Override
        public ProfModel[] newArray(int size) {
            return new ProfModel[size];
        }
    };

    public String getImge() {
        return imge;
    }

    public void setImge(String imge) {
        this.imge = imge;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProf() {
        return prof;
    }

    public void setProf(String prof) {
        this.prof = prof;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imge);
        dest.writeString(address);
        dest.writeString(name);
        dest.writeString(prof);
        dest.writeString(phone);
    }
}
