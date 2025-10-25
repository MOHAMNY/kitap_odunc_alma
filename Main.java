import java.util.*;
import java.time.LocalDate;

class Kitap {
    private int id;
    private String ad;
    private String yazar;
    private boolean mevcut;

    public Kitap(int id, String ad, String yazar) {
        this.id = id;
        this.ad = ad;
        this.yazar = yazar;
        this.mevcut = true;
    }

    public int getId() { return id; }
    public String getAd() { return ad; }
    public String getYazar() { return yazar; }
    public boolean isMevcut() { return mevcut; }

    public void setMevcut(boolean mevcut) { this.mevcut = mevcut; }

    @Override
    public String toString() {
        return "Kitap ID: " + id + ", Ad: " + ad + ", Yazar: " + yazar + ", Mevcut: " + mevcut;
    }
}

class Uye {
    private int id;
    private String ad;
    private String soyad;

    public Uye(int id, String ad, String soyad) {
        this.id = id;
        this.ad = ad;
        this.soyad = soyad;
    }

    public int getId() { return id; }
    public String getAd() { return ad; }
    public String getSoyad() { return soyad; }

    @Override
    public String toString() {
        return "Üye ID: " + id + ", Ad: " + ad + " " + soyad;
    }
}

class OduncKaydi {
    private Uye uye;
    private Kitap kitap;
    private LocalDate oduncTarihi;
    private LocalDate iadeTarihi;

    public OduncKaydi(Uye uye, Kitap kitap) {
        this.uye = uye;
        this.kitap = kitap;
        this.oduncTarihi = LocalDate.now();
        this.iadeTarihi = null;
    }

    public Uye getUye() { return uye; }
    public Kitap getKitap() { return kitap; }
    public LocalDate getOduncTarihi() { return oduncTarihi; }
    public LocalDate getIadeTarihi() { return iadeTarihi; }

    public void setIadeTarihi(LocalDate iadeTarihi) {
        this.iadeTarihi = iadeTarihi;
    }

    @Override
    public String toString() {
        return "Üye: " + uye.getAd() + " - Kitap: " + kitap.getAd() + 
               " | Ödünç Tarihi: " + oduncTarihi + 
               (iadeTarihi != null ? " | İade Tarihi: " + iadeTarihi : " | Henüz iade edilmedi");
    }
}

class Kutuphane {
    private List<Kitap> kitaplar = new ArrayList<>();
    private List<Uye> uyeler = new ArrayList<>();
    private List<OduncKaydi> oduncler = new ArrayList<>();

    // Kitap ve üye ekleme
    public void kitapEkle(Kitap kitap) {
        kitaplar.add(kitap);
    }

    public void uyeEkle(Uye uye) {
        uyeler.add(uye);
    }

    // Ödünç alma işlemi
    public void kitapOduncAl(int uyeId, int kitapId) {
        Uye uye = uyeler.stream().filter(u -> u.getId() == uyeId).findFirst().orElse(null);
        Kitap kitap = kitaplar.stream().filter(k -> k.getId() == kitapId).findFirst().orElse(null);

        if (uye == null || kitap == null) {
            System.out.println("Üye veya kitap bulunamadı!");
            return;
        }

        if (!kitap.isMevcut()) {
            System.out.println("Kitap şu anda ödünçte!");
            return;
        }

        kitap.setMevcut(false);
        OduncKaydi kayit = new OduncKaydi(uye, kitap);
        oduncler.add(kayit);
        System.out.println("Kitap ödünç verildi: " + kitap.getAd() + " → " + uye.getAd());
    }

    // İade işlemi
    public void kitapIadeEt(int uyeId, int kitapId) {
        for (OduncKaydi kayit : oduncler) {
            if (kayit.getUye().getId() == uyeId && kayit.getKitap().getId() == kitapId && kayit.getIadeTarihi() == null) {
                kayit.setIadeTarihi(LocalDate.now());
                kayit.getKitap().setMevcut(true);
                System.out.println("Kitap iade edildi: " + kayit.getKitap().getAd());
                return;
            }
        }
        System.out.println("İade işlemi başarısız: Kayıt bulunamadı.");
    }

    // Ödünçte olan kitapları listeleme
    public void aktifOduncleriListele() {
        System.out.println("Aktif ödünçler:");
        for (OduncKaydi kayit : oduncler) {
            if (kayit.getIadeTarihi() == null) {
                System.out.println(kayit);
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Kutuphane kutuphane = new Kutuphane();

        // Örnek veriler
        Kitap k1 = new Kitap(1, "Sefiller", "Victor Hugo");
        Kitap k2 = new Kitap(2, "Küçük Prens", "Antoine de Saint-Exupéry");
        Uye u1 = new Uye(1, "Ali", "Yılmaz");
        Uye u2 = new Uye(2, "Ayşe", "Demir");

        kutuphane.kitapEkle(k1);
        kutuphane.kitapEkle(k2);
        kutuphane.uyeEkle(u1);
        kutuphane.uyeEkle(u2);

        // Test işlemleri
        kutuphane.kitapOduncAl(1, 1);
        kutuphane.kitapOduncAl(2, 2);
        kutuphane.kitapIadeEt(1, 1);

        kutuphane.aktifOduncleriListele();
    }
}
