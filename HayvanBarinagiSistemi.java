import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
class Hayvan {
    private String ad;
    private String tur;
    private Date kayitTarihi;
    private float yas;
    private List<String> asiAdlari; // Linked list for vaccine names
    private String mahalle;
    public Hayvan(String ad, String tur, float yas) {
        this.ad = ad;
        this.tur = tur;
        this.yas = yas;
        this.kayitTarihi = new Date();
        this.asiAdlari = new LinkedList<>(); // Initializing linked list for vaccines
        this.mahalle = "";
    }

    public String getAd() {
        return ad;
    }

    public String getTur() {
        return tur;
    }

    public Date getKayitTarihi() {
        return kayitTarihi;
    }

    public float getYas() {
        return yas;
    }

    public List<String> getAsiAdlari() {
        return asiAdlari;
    }

    public void asiEkle(String asiAdi) {
        asiAdlari.add(asiAdi);
        System.out.println(asiAdi + " aşısı " + ad + " adlı hayvana eklendi.");
    }
        public String getMahalle () {
            return mahalle;
        }

        public void setMahalle(String mahalle) {
            this.mahalle = mahalle;
        }
    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return "Hayvan [ad=" + ad + ", tur=" + tur + ", kayitTarihi=" + dateFormat.format(kayitTarihi)
                + ", yas=" + yas + ", asiAdlari=" + asiAdlari + "]";
    }
}

class HayvanBarinagi {
    private List<Hayvan> hayvanlar;

    public HayvanBarinagi() {
        this.hayvanlar = new ArrayList<>();
    }

    public void hayvanEkle(Hayvan hayvan) {
        hayvanlar.add(hayvan);
        System.out.println(hayvan.getAd() + " adlı hayvan barınağa eklendi. Kayıt tarihi: " + hayvan.getKayitTarihi());
    }

    public void hayvanlariListele() {
        System.out.println("Barınaktaki Hayvanlar:");
        for (Hayvan hayvan : hayvanlar) {
            System.out.println(hayvan);
        }
    }

    public Hayvan hayvanAra(String ad) {
        for (Hayvan hayvan : hayvanlar) {
            if (hayvan.getAd().equalsIgnoreCase(ad)) {
                return hayvan;
            }
        }
        return null;
    }

    public void hayvanSil(String ad) {
        Hayvan hayvan = hayvanAra(ad);
        if (hayvan != null) {
            hayvanlar.remove(hayvan);
            System.out.println(ad + " adlı hayvan barınaktan silindi.");
        } else {
            System.out.println(ad + " adlı hayvan bulunamadı.");
        }
    }
    public List<Hayvan> getHayvanlar() {
        return hayvanlar;
    }
}



public class HayvanBarinagiSistemi extends JFrame {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin123";
    private final HayvanBarinagi barinak;
    private JFrame listelemePenceresi;
    private JTextArea listelemeMetniAlani;

    public HayvanBarinagiSistemi() {
        super("Hayvan Barınak Sistemi");
        this.barinak = new HayvanBarinagi();
        this.listelemePenceresi = null;
        this.listelemeMetniAlani = null;
        arayuzOlustur();
    }

    private void arayuzOlustur() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setLayout(new BorderLayout());

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 2));
        JLabel kullaniciAdiLabel = new JLabel("     Kullanıcı Adı:");
        JTextField kullaniciAdiAlan = new JTextField();
        JLabel sifreLabel = new JLabel("     Şifre:");
        JPasswordField sifreAlan = new JPasswordField();
        JButton girisButton = new JButton("Giriş");

        loginPanel.add(kullaniciAdiLabel);
        loginPanel.add(kullaniciAdiAlan);
        loginPanel.add(sifreLabel);
        loginPanel.add(sifreAlan);
        loginPanel.add(new JLabel()); // Boşluk için boş etiket
        loginPanel.add(girisButton);

        add(loginPanel, BorderLayout.CENTER);

        JTextArea sonucMetinAlani = new JTextArea();
        add(new JScrollPane(sonucMetinAlani), BorderLayout.SOUTH);

        girisButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String girilenKullaniciAdi = kullaniciAdiAlan.getText();
                String girilenSifre = new String(sifreAlan.getPassword());

                if (kullaniciDogrula(girilenKullaniciAdi, girilenSifre)) {
                    loginPaneliKaldir();
                    menuyuGoster(sonucMetinAlani);
                } else {
                    sonucMetinAlani.append("Hatalı kullanıcı adı veya şifre. Tekrar deneyin.\n");
                }
            }
        });
    }

    private void menuyuGoster(JTextArea sonucMetinAlani) {
        getContentPane().removeAll();
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(2, 3));
        String[] dugmeMetinleri = {"Hayvan Ekle", "Hayvanları Listele", "Hayvan Ara", "Hayvan Sil", "Aşı Bilgisi Ekle", "Çıkış"};

        for (String dugmeMetni : dugmeMetinleri) {
            JButton dugme = new JButton(dugmeMetni);
            menuPanel.add(dugme);
            dugme.addActionListener(new MenuDugmeDinleyici(sonucMetinAlani, dugmeMetni, this.barinak));
        }

        add(menuPanel, BorderLayout.CENTER);
        revalidate();
    }

    private void loginPaneliKaldir() {
        getContentPane().removeAll();
        revalidate();
        repaint();
    }

    private class MenuDugmeDinleyici implements ActionListener {
        private final JTextArea sonucMetinAlani;
        private final String dugmeMetni;
        private final HayvanBarinagi barinak;

        public MenuDugmeDinleyici(JTextArea sonucMetinAlani, String dugmeMetni, HayvanBarinagi barinak) {
            this.sonucMetinAlani = sonucMetinAlani;
            this.dugmeMetni = dugmeMetni;
            this.barinak = barinak;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (dugmeMetni) {
                case "Hayvan Ekle":
                    hayvanEkle();
                    break;
                case "Hayvanları Listele":
                    hayvanlariListele();
                    break;
                case "Hayvan Ara":
                    hayvanAra();
                    break;
                case "Hayvan Sil":
                    hayvanSil();
                    break;
                case "Aşı Bilgisi Ekle":
                    asiBilgisiEkle();
                    break;
                case "Çıkış":
                    cikisYap();
                    break;
                default:
                    sonucMetinAlani.append("Bilinmeyen bir seçenek seçildi\n");
            }
        }

        private void hayvanEkle() {
            String ad = JOptionPane.showInputDialog("Hayvan adı:");
            // Türü seçmek için bir JComboBox oluştur
            String[] turler = {"Kedi", "Köpek", "Kuş"};
            JComboBox<String> turSecim = new JComboBox<>(turler);

            // Kullanıcıdan türü seçmesini iste
            JOptionPane.showOptionDialog(
                    null,
                    turSecim,
                    "Hayvan Türü Seç",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    null,
                    null
            );

            // Seçilen türü al
            String secilenTur = (String) turSecim.getSelectedItem();

            float yas = Float.parseFloat(JOptionPane.showInputDialog("Hayvan yaş:"));
            // Bulunduğu ilçeyi al
            String[] mahalleler = {"Adnan Menderes Mahallesi", "Bağlarbaşı Mahallesi", "Bahçelievler Mahallesi",
                    "Bayraktepe Mahallesi", "Dere Mahallesi", "Fevzi Çakmak Mahallesi", "Gazi Osman Paşa Mahallesi",
                    "İsmet Paşa Mahallesi", "Kazım Karabekir Mahallesi", "Merkez Mahallesi", "Mustafa Kemal Paşa Mahallesi",
                    "Özden Mahallesi", "Paşakent Mahallesi", "Rüstem Paşa Mahallesi", "Seyrantepe Mahallesi",
                    "Süleyman Bey Mahallesi"};

            JComboBox<String> mahalleSecim = new JComboBox<>(mahalleler);

            // Kullanıcıdan mahalle seçmesini iste
            JOptionPane.showOptionDialog(
                    null,
                    mahalleSecim,
                    "Bulunduğu Mahalle Seç",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    null,
                    null
            );

            // Seçilen mahalleyi al
            String secilenMahalle = (String) mahalleSecim.getSelectedItem();



            String asiAdi = JOptionPane.showInputDialog("Aşı adı:");

            Hayvan yeniHayvan = new Hayvan(ad, secilenTur, yas);
            barinak.hayvanEkle(yeniHayvan);
            yeniHayvan.asiEkle(asiAdi);
            barinak.hayvanEkle(yeniHayvan);
            // Sonucu ekrana yaz
            JOptionPane.showMessageDialog(null, ad + " adlı " + secilenTur + " barınağa eklendi. Kayıt tarihi: " + yeniHayvan.getKayitTarihi() + "\n" +
                    "Bulunduğu mahalle: " + secilenMahalle + "\n" +
                    asiAdi + " aşısı " + ad + " adlı hayvana eklendi.");
        }

        private void hayvanlariListele() {

            sonucMetinAlani.setText(""); // Önceki içeriği temizle
            // Yeni bir pencere oluştur
            if (listelemePenceresi == null) {
                listelemePenceresi = new JFrame("Barınaktaki Hayvanlar");
                listelemePenceresi.setSize(400, 300);

                listelemeMetniAlani = new JTextArea();
                listelemeMetniAlani.setEditable(false);

                JScrollPane scrollPane = new JScrollPane(listelemeMetniAlani);
                listelemePenceresi.add(scrollPane);
            }
            listelemeMetniAlani.setText("");
            // Hayvanların listelendiği HashSet
            Set<Hayvan> hayvanSet = new HashSet<>();

            // Hayvanları listele ve içeriği güncelle
            listelemeMetniAlani.append("Barınaktaki Hayvanlar:\n");
            for (Hayvan hayvan : barinak.getHayvanlar()) {
                // Eğer hayvan sette yoksa ekleyip içeriği güncelle
                if (!hayvanSet.contains(hayvan)) {
                    hayvanSet.add(hayvan);
                    listelemeMetniAlani.append(hayvan + "\n");
                }

            // Yeni pencereyi görünür yap
            listelemePenceresi.setVisible(true);
        }}

        private void hayvanAra() {
            // Kullanıcıdan aranacak hayvan adını al
            String arananAd = JOptionPane.showInputDialog("Aranacak hayvanın adı:");

            // Hayvanı ara ve sonucu ekrana yaz
            Hayvan arananHayvan = barinak.hayvanAra(arananAd);
            if (arananHayvan != null) {
                // Eğer hayvan bulunduysa, yeni pencere oluştur
                JFrame aramaPenceresi = new JFrame("Hayvan Arama Sonucu");
                aramaPenceresi.setSize(400, 150);

                // Sonuç metni alanını oluştur
                JTextArea sonucMetniAlani = new JTextArea();
                sonucMetniAlani.setEditable(false);

                // Sonuçları metin alanına ekle
                sonucMetniAlani.append("Aranan hayvan bulundu:\n\n");
                sonucMetniAlani.append(arananHayvan.toString());

                // Pencereye metin alanını ekle
                aramaPenceresi.add(sonucMetniAlani);

                // Pencereyi görünür yap
                aramaPenceresi.setVisible(true);
            } else {
                // Eğer hayvan bulunamadıysa, sonucu ekrana yaz
                JOptionPane.showMessageDialog(null, arananAd + " adlı hayvan bulunamadı.", "Hayvan Arama Hatası", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void hayvanSil() {
            // Kullanıcıdan silinecek hayvan adını al
            String silinecekAd = JOptionPane.showInputDialog("Silinecek hayvanın adı:");

            // Hayvanı sil ve sonucu ekrana yaz
            Hayvan silinenHayvan = barinak.hayvanAra(silinecekAd);
            if (silinenHayvan != null) {
                // Eğer hayvan bulunduysa, silme işlemini gerçekleştir
                barinak.hayvanSil(silinecekAd);

                // Yeni pencere oluştur
                JFrame silmePenceresi = new JFrame("Hayvan Silme Sonucu");
                silmePenceresi.setSize(300, 100);

                // Sonuç metni alanını oluştur
                JTextArea sonucMetniAlani = new JTextArea();
                sonucMetniAlani.setEditable(false);

                // Sonuçları metin alanına ekle
                sonucMetniAlani.append(silinenHayvan.getAd() + " adlı hayvan barınaktan silindi.");

                // Pencereye metin alanını ekle
                silmePenceresi.add(sonucMetniAlani);

                // Pencereyi görünür yap
                silmePenceresi.setVisible(true);
            } else {
                // Eğer hayvan bulunamadıysa, sonucu ekrana yaz
                JOptionPane.showMessageDialog(null, silinecekAd + " adlı hayvan bulunamadı.", "Hayvan Silme Hatası", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void asiBilgisiEkle() {
            // Kullanıcıdan aşı ekleyeceği hayvanın adını al
            String hayvanAdi = JOptionPane.showInputDialog("Aşı ekleyeceğiniz hayvanın adı:");

            // Hayvanı bul ve aşı bilgisi ekleyeceğimiz pencereyi oluştur
            Hayvan hayvan = barinak.hayvanAra(hayvanAdi);
            if (hayvan != null) {
                // Eğer hayvan bulunduysa, yeni pencere oluştur
                JFrame asiEklePenceresi = new JFrame("Aşı Bilgisi Ekleme");
                asiEklePenceresi.setSize(400, 200);

                // Sonuç metni alanını oluştur
                JTextArea sonucMetniAlani = new JTextArea();
                sonucMetniAlani.setEditable(false);

                // Aşı adını al
                String yeniAsiAdi = JOptionPane.showInputDialog("Eklemek istediğiniz aşı adı:");

                // Aşıyı ekleyip sonucu metin alanına ekle
                hayvan.asiEkle(yeniAsiAdi);
                sonucMetniAlani.append(yeniAsiAdi + " aşı bilgisi " + hayvanAdi + " adlı hayvana eklendi.\n\n");

                // Pencereye metin alanını ekle
                asiEklePenceresi.add(sonucMetniAlani);

                // Pencereyi görünür yap
                asiEklePenceresi.setVisible(true);
            } else {
                // Eğer hayvan bulunamadıysa, sonucu ekrana yaz
                JOptionPane.showMessageDialog(null, hayvanAdi + " adlı hayvan bulunamadı.", "Aşı Bilgisi Ekleme Hatası", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void cikisYap() {
            sonucMetinAlani.append("Programdan çıkılıyor...\n");
            System.exit(0);
        }
    }

    private boolean kullaniciDogrula(String kullaniciAdi, String sifre) {
        return USERNAME.equals(kullaniciAdi) && PASSWORD.equals(sifre);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new HayvanBarinagiSistemi().setVisible(true);
            }
        });
    }
}

