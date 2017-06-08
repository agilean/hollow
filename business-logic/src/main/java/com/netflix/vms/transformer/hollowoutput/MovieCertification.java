package com.netflix.vms.transformer.hollowoutput;


public class MovieCertification implements Cloneable {

    public int certificationSystemId = java.lang.Integer.MIN_VALUE;
    public int ratingId = java.lang.Integer.MIN_VALUE;
    public int maturityLevel = java.lang.Integer.MIN_VALUE;
    public MovieRatingReason ratingReason = null;

    public boolean equals(Object other) {
        if(other == this)  return true;
        if(!(other instanceof MovieCertification))
            return false;

        MovieCertification o = (MovieCertification) other;
        if(o.certificationSystemId != certificationSystemId) return false;
        if(o.ratingId != ratingId) return false;
        if(o.maturityLevel != maturityLevel) return false;
        if(o.ratingReason == null) {
            if(ratingReason != null) return false;
        } else if(!o.ratingReason.equals(ratingReason)) return false;
        return true;
    }

    public int hashCode() {
        int hashCode = 1;
        hashCode = hashCode * 31 + certificationSystemId;
        hashCode = hashCode * 31 + ratingId;
        hashCode = hashCode * 31 + maturityLevel;
        hashCode = hashCode * 31 + (ratingReason == null ? 1237 : ratingReason.hashCode());
        return hashCode;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("MovieCertification{");
        builder.append(",certificationSystemId=").append(certificationSystemId);
        builder.append(",ratingId=").append(ratingId);
        builder.append(",maturityLevel=").append(maturityLevel);
        builder.append(",ratingReason=").append(ratingReason);
        builder.append("}");
        return builder.toString();
    }

    public MovieCertification clone() {
        try {
            MovieCertification clone = (MovieCertification)super.clone();
            clone.__assigned_ordinal = -1;
            return clone;
        } catch (CloneNotSupportedException cnse) { throw new RuntimeException(cnse); }
    }

    @SuppressWarnings("unused")
    private long __assigned_ordinal = -1;
}
