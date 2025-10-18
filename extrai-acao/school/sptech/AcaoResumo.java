package school.sptech;

public class AcaoResumo {
    private String name;
    private String sector;
    private Logo logo;

    public String getName() { return name; }
    public String getSector() { return sector; }
    public Logo getLogo() { return logo; }

    @Override
    public String toString() {
        return "AcaoResumo{" +
                "name='" + name + '\'' +
                ", sector='" + sector + '\'' +
                ", logo=" + logo +
                '}';
    }
}
