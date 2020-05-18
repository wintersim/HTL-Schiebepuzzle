package cc.catgasm.HTLWSlidingPuzzle.parcelable;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/*Klasse dient nur zum Datenaustausch zwischen MainActivity und GameActivity*/
public class ImageParcelable implements Parcelable {
    private Uri customImage;
    private int officialImageId;
    private int imageType;

    public static final int CUSTOM_IMAGE = 1;
    public static final int OFFICIAL_IMAGE = 2;

    public ImageParcelable(Uri customImage) {
        this.customImage = customImage;
        imageType = CUSTOM_IMAGE;
    }

    public ImageParcelable(int officialImageId) {
        this.officialImageId = officialImageId;
        imageType = OFFICIAL_IMAGE;
    }

    protected ImageParcelable(Parcel in) {
        customImage = in.readParcelable(Uri.class.getClassLoader());
        officialImageId = in.readInt();
        imageType = in.readInt();
    }

    public static final Creator<ImageParcelable> CREATOR = new Creator<ImageParcelable>() {
        @Override
        public ImageParcelable createFromParcel(Parcel in) {
            return new ImageParcelable(in);
        }

        @Override
        public ImageParcelable[] newArray(int size) {
            return new ImageParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(customImage, i);
        parcel.writeInt(officialImageId);
        parcel.writeInt(imageType);
    }

    public Uri getCustomImage() {
        return customImage;
    }

    public int getOfficialImageId() {
        return officialImageId;
    }

    public int getImageType() {
        return imageType;
    }
}
