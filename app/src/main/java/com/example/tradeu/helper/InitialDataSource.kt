package com.example.tradeu.helper

import com.example.tradeu.R
import com.example.tradeu.data.entities.*
import com.example.tradeu.formatToDate


object InitialDataSource {
    //tabelUser
    private val listIdUser = listOf(123, 124, 125, 126, 127, 128, 129, 130)
    private val listUsername = listOf("Aliyah123", "fajar456", " Alif556", "elna334", "Dila1234", "Karmila764", "Rima984", "Rina8327")
    private val listPassword = listOf("s12357", "kl2354", "kc5698", "Rl3479", "t627", "h628", "k3768", "b3273")
    private val listFotoProfil = listOf(R.drawable.pp_aliyah, R.drawable.pp_fajar, R.drawable.pp_alif, R.drawable.pp_elna, R.drawable.pp_dila, R.drawable.pp_karmila, R.drawable.pp_rima,  R.drawable.pp_rina)
    private val listNama = listOf("Aliyah", "Fajar", "Alif", "elna", "Dila",  "Karmila", "Rima", "Rina")
    private val listSaldo = listOf(50000, 100000, 200000, 70000, 80000, 90000, 150000, 120000)
    private val listAlamat = listOf("Padang", "Solok", "Pariaman", "Jakarta", "Padang Panjang",  "Aceh", "Medan", "Bandung")

    fun getListUser():List<User>{
        val listUser = mutableListOf<User>()
        for(i in listIdUser.indices){
            val newUser = User(
                listIdUser[i].toLong(),
                listUsername[i],
                listPassword[i],
                listFotoProfil[i],
                listNama[i],
                listSaldo[i].toLong(),
                listAlamat[i])
            listUser.add(newUser)
        }
        return listUser
    }

//tabelBarang
    private val listIdPenjual = listOf(127, 128, 129, 130)
    private val listIdBarang = listOf(12, 13, 14, 15)
    private val listNamaBarang = listOf("Kaos", "Kemeja", "Celana Putih", "Baju koko")
    private val listFotoProduk = listOf(R.drawable.barang, R.drawable.kemeja, R.drawable.celana_putih, R.drawable.baju_koko)
    private val listHargaBarang = listOf(45000, 100000, 55000, 150000)
    private val listStok = listOf(100, 50, 75 , 76)
    private val listTglPublish = listOf("2022/03/05".formatToDate()!!,"2023/04/20".formatToDate()!!,"2022/11/21".formatToDate()!!,"2022/11/30".formatToDate()!!)
    private val listDecription = listOf(
        "Kaos distro dari UNKL347\nSeri dari UNKL347, kaos bodysize dengan cotton 30's. Berbahan lebih ringan, sehingga nyaman digunakan ketika udara panas.\n\nBahan : Soft Cotton Combed 30s\nSize chart : lihat pada slide gambar, tolerasi size antara 1-3 cm\n\nInformasi tambahan :\nKami menerima refund/return jika, barang tidak sesuai / cacat produksi dengan syarat menyertakan video unboxing secara detail\nPengiriman dikirim dari Warehouse baru UNKL347 di Karanganyar (SOLO)",
        "Kemeja dengan bahan linen berkualitas tinggi menjamin penampilan yang elegan dan pas untuk setiap acara santai. Tampil dengan gaya relaxed bersama koleksi kemeja linen Day to Day, yang merupakan tambahan sempurna untuk outfit smart-casual kamu.\n\nSize Chart\nDada x Panjang x Bahu x Lengan (cm)\nS : 54 x 69 x 49 x 23\nM : 56 x 71 x 50 x 24\nL : 58 x 73 x 51 x 25\nXL : 60 x 75 x 52 x 26\nXXL : 62 x 77 x 53 x 27\n\nCara Perawatan :\n- Cuci dengan warna yang senada & suhu air yang tidak terlalu panas\n- Usahakan membalikan luar-dalam pakaian pada saat pencucian\n- Hindari penggunaan pemutih\n- Hindari setrika dengan suhu yang terlalu panas\n\nPeraturan Pengajuan Komplain :\n- Wajib menyertakan foto & video unboxing\n- Pengajuan komplain maksimal 3 hari setelah produk di terima",
        "selamat datang di lanuna official..🙏\n\ncelana kasual panjang pria recomended banget\npas buat di pakai saat bersantai di rumah atau berkumpul aktifitas lain nya\ndengan bahan yang adem banget membuat nyaman saat memakai\ndengan ukuran pinggang karet all size membuat kamu lebih lega tanpa khwatir dengan naik nya berat tubuh\n\nspek\npanjang 102 cm\npinggang max 110 cm\nwarna putih motif salur\n\nyuuk buruan diorder stock terbatas",
        "koko model semi jas.\nsaku ada 3. atas dan samping kanan kiri.\nmatrial: katun toyobo import.\n\nmenerima seragam dan grosir\n\ndetail ukuran:\nsize: S. lingkara dada 104cm. panjang baju 70cm. panjang lengan 56cm.\nsize: M. lingkar dada 110cm. panjang baju 72cm. panjang lengan 58cm.\nsize: L. lingkar dada 114 cm. panjang baju 74cm. panjang lengan 60cm.\nsize: XL. lingkar dada 122cm. panjang baju 77cm. panjang lengan 62cm\n\nBEDA CAHAYA & PENGATURAN CAHAYA\nmengakibatkan warna sedikit berbeda dengan fisik. harap maklum.\nfoto kami buat mendetail fisik stok 100%..\nsemua pruduk kami dibuat didalam negri. karya anak bangsa. LOKAL & ORIGINAL. KUALITAS. tidak kalah dengan luar negri.. nyaman dipakai karna menggunakan pola yang bagus. dan jahitan rapi\n\nada beragam kualitas & harga berbeda.\ntapi kami berusaha memberi HARGA & KUALITAS YANG BAIK."
    )

    fun getListItems():List<Item>{
        val listItems = mutableListOf<Item>()
        for(i in listIdBarang.indices){
            val newItem = Item(
                listIdBarang[i].toLong(),
                listIdPenjual[i].toLong(),
                listNamaBarang[i],
                listFotoProduk[i],
                listHargaBarang[i].toLong(),
                listStok[i].toShort(),
                listTglPublish[i],
                listDecription[i])
            listItems.add(newItem)
        }
        return listItems
    }


//tabel tranksaksi
    private val listNoTranksaksi = listOf(1, 2, 3, 4)
    private val listTglTranksaksi = listOf("2022/08/25".formatToDate()!!,"2023/05/2".formatToDate()!!,"2023/03/11".formatToDate()!!,"2023/05/10".formatToDate()!!)
    private val listIdPembeli = listOf(123, 124, 125, 126)
    private val listIdBarangDibeli = listOf(12, 13, 14, 15)
    private val listJumlah = listOf(5, 4, 3, 2)
    private val listHarga = listOf(45000, 99000, 150000, 100000)
    // val listIdUkuran = listOf(1, 1, 3, 4)

    fun getListTransaction():List<Transaction>{
        val listTransaction = mutableListOf<Transaction>()
        for(i in listNoTranksaksi.indices){
            val newTransaction = Transaction(
                listNoTranksaksi[i].toLong(),
                listTglTranksaksi[i],
                listIdPembeli[i].toLong(),
                listIdBarangDibeli[i].toLong(),
                listJumlah[i].toShort(),
                listHarga[i].toLong(),
                listIdUkuran[i].toLong()
            )
            listTransaction.add(newTransaction)
        }
        return listTransaction

    }

    //tabel favorite
    private val listIdUserFavorite = listOf(123, 123, 124, 125, 126, 126)
    private val listIdBarangFavorite = listOf(12, 13, 13, 14, 14 ,15)
    fun getListFavorites():List<Favorite>{
        val listFavorite = mutableListOf<Favorite>()
        for(i in listIdUserFavorite.indices){
            val newFavorite = Favorite(listIdUserFavorite[i].toLong(), listIdBarangFavorite[i].toLong())
            listFavorite.add(newFavorite)
        }
        return listFavorite
    }

    //tabel ukuran pakaian
    private val listIdUkuran = listOf(1, 2, 3, 4)
    private val listUkuran = listOf ("S", "M", "L", "XL")

    fun getListItemSizes():List<ItemSize>{
        val listItemSizes = mutableListOf<ItemSize>()
        for(i in listIdUkuran.indices){
            val newItemSize = ItemSize(listIdUkuran[i].toLong(), listUkuran[i])
            listItemSizes.add(newItemSize)
        }
        return listItemSizes
    }











}