package ru.project.market_auction.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import ru.project.market_auction.models.auctions.Auction;
import ru.project.market_auction.models.auctions.AuctionBid;
import ru.project.market_auction.models.auctions.AuctionDetail;
import ru.project.market_auction.models.books.Book;
import ru.project.market_auction.models.sales.BookSale;
import ru.project.market_auction.models.users.User;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FileService {
    public static void getCartCheckout(HttpServletResponse response, Map<BookSale, Integer> bookSaleCountMap, User user) throws Exception{
        // Создаем PDF-документ
        Document document = new Document();
        response.setContentType("application/pdf");
        PdfWriter.getInstance(document, response.getOutputStream());

        // Открываем документ для записи
        document.open();

        BaseFont bf = BaseFont.createFont("/static/fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(bf, 12, Font.NORMAL);

        // Добавляем содержимое в PDF
        document.add(new Paragraph(new Phrase("Название сайта: Маркет и Аукцион", font)));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("ru"));
        String formattedDate = dateFormat.format(new Date());
        document.add(new Paragraph(new Phrase("Дата оформления: " + formattedDate, font)));
        document.add(new Paragraph(new Phrase("Пользователь: " + user.toString(), font)));
        document.add(new Paragraph("\n"));
        Paragraph centeredOrder = new Paragraph(new Phrase("Заказ № " + new Date().getTime(), font));
        centeredOrder.setAlignment(Element.ALIGN_CENTER);
        document.add(centeredOrder);
        document.add(new Paragraph("\n"));

        // Создаем таблицу
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1f, 4f, 1f, 2f, 2f}); // Устанавливаем ширину колонок
        table.addCell(new PdfPCell(new Phrase("№", font)));
        table.addCell(new PdfPCell(new Phrase("Название", font)));
        table.addCell(new PdfPCell(new Phrase("Кол-во", font)));
        table.addCell(new PdfPCell(new Phrase("Цена", font)));
        table.addCell(new PdfPCell(new Phrase("Сумма", font)));

        int rowNumber = 1;
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (Map.Entry<BookSale, Integer> entry : bookSaleCountMap.entrySet()) {
            BookSale bookSale = entry.getKey();
            Integer quantity = entry.getValue();
            BigDecimal itemTotalPrice = bookSale.getPrice().multiply(new BigDecimal(quantity));

            table.addCell(new PdfPCell(new Phrase(String.valueOf(rowNumber++), font)));
            table.addCell(new PdfPCell(new Phrase(bookSale.getBook().getTitle(), font)));
            table.addCell(new PdfPCell(new Phrase(quantity.toString() +" шт.", font)));
            table.addCell(new PdfPCell(new Phrase(bookSale.getPrice().toString(), font)));
            table.addCell(new PdfPCell(new Phrase(itemTotalPrice.toString(), font)));

            totalPrice = totalPrice.add(itemTotalPrice);
        }

        document.add(table);

        Paragraph totalSumParagraph = new Paragraph(new Phrase("Итоговая сумма: " + totalPrice, font));
        totalSumParagraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(totalSumParagraph);

        // Закрываем документ
        document.close();
    }

    public static void getAuctionResults(HttpServletResponse response, Auction auction) throws Exception{
        // Создаем PDF-документ
        Document document = new Document();
        response.setContentType("application/pdf");
        PdfWriter.getInstance(document, response.getOutputStream());

        // Открываем документ для записи
        document.open();

        BaseFont bf = BaseFont.createFont("/static/fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(bf, 12, Font.NORMAL);

        // Добавляем содержимое в PDF
        document.add(new Paragraph(new Phrase("Название сайта: Маркет и Аукцион", font)));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("ru"));
        String formattedDate = dateFormat.format(new Date());
        document.add(new Paragraph(new Phrase("Дата оформления отчета: " + formattedDate, font)));
        document.add(new Paragraph(new Phrase("Владелец: " + auction.getUser().toString(), font)));
        document.add(new Paragraph("\n"));
        Paragraph centeredOrder = new Paragraph(new Phrase("Аукцион № " + new Date().getTime(), font));
        centeredOrder.setAlignment(Element.ALIGN_CENTER);
        document.add(centeredOrder);
        document.add(new Paragraph("\n"));
        document.add(new Paragraph(new Phrase("Название аукциона - " + getAuctionTitle(auction), font)));
        document.add(new Paragraph(new Phrase("Дата начала: " + auction.getBegTime(), font)));
        document.add(new Paragraph(new Phrase("Дата окончания: " + auction.getEndTime(), font)));
        if(auction.getAuctionDuration() != null){
            document.add(new Paragraph(new Phrase("Длительность - " + auction.getAuctionDuration(), font)));
        }
        document.add(new Paragraph("\n"));
        document.add(new Paragraph(new Phrase("Книги выставленные на аукционе:", font)));
        document.add(new Paragraph("\n"));

        // Создаем таблицу ставок
        PdfPTable tableBooks = new PdfPTable(3);
        tableBooks.setWidthPercentage(100);
        tableBooks.setWidths(new float[]{1f, 5f, 3f}); // Устанавливаем ширину колонок
        tableBooks.addCell(new PdfPCell(new Phrase("№", font)));
        tableBooks.addCell(new PdfPCell(new Phrase("Название", font)));
        tableBooks.addCell(new PdfPCell(new Phrase("Издатель", font)));

        int rowNumber = 1;
        for (AuctionDetail detail : auction.getAuctionDetails()) {
            tableBooks.addCell(new PdfPCell(new Phrase(String.valueOf(rowNumber++), font)));
            tableBooks.addCell(new PdfPCell(new Phrase(detail.getBook().getTitle(), font)));
            tableBooks.addCell(new PdfPCell(new Phrase(detail.getBook().getPublisher().getName()
                    + "-" + detail.getBook().getPublicationYear(), font)));
        }
        document.add(tableBooks);

        document.add(new Paragraph("\n"));
        document.add(new Paragraph(new Phrase("Ставки на аукцион:", font)));
        document.add(new Paragraph("\n"));

        // Создаем таблицу ставок
        PdfPTable tableBid = new PdfPTable(4);
        tableBid.setWidthPercentage(100);
        tableBid.setWidths(new float[]{1f, 4f, 2f, 2f}); // Устанавливаем ширину колонок
        tableBid.addCell(new PdfPCell(new Phrase("№", font)));
        tableBid.addCell(new PdfPCell(new Phrase("Пользователь", font)));
        tableBid.addCell(new PdfPCell(new Phrase("Ставка", font)));
        tableBid.addCell(new PdfPCell(new Phrase("Дата", font)));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        rowNumber = 1;
        for (AuctionBid bid : auction.getAuctionBids()) {
            tableBid.addCell(new PdfPCell(new Phrase(String.valueOf(rowNumber++), font)));
            tableBid.addCell(new PdfPCell(new Phrase(bid.getUser().toString(), font)));
            tableBid.addCell(new PdfPCell(new Phrase(bid.getBidAmount().toString(), font)));
            tableBid.addCell(new PdfPCell(new Phrase(bid.getBidTime().format(formatter), font)));
        }
        document.add(tableBid);

        AuctionBid maxBid = getMaxBid(auction.getAuctionBids());
        Paragraph totalSumParagraph = new Paragraph(new Phrase("Лучшая ставка - " + maxBid.getBidAmount()
                + "р., от пользователя " + maxBid.getUser().toString(), font));
        totalSumParagraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(totalSumParagraph);

        // Закрываем документ
        document.close();
    }

    private static String getAuctionTitle(Auction auction){
        StringBuilder auctionTitle = new StringBuilder();
        for(AuctionDetail detail : auction.getAuctionDetails()){
            auctionTitle.append(detail.getBook().getTitle()).append(", ");
        }
        // Удаляем последнюю запятую и пробел, заменяем на точку
        if (auctionTitle.length() > 0) {
            auctionTitle.setLength(auctionTitle.length() - 2); // удаляем ", "
            auctionTitle.append('.');
        }
        return auctionTitle.toString();
    }

    private static AuctionBid getMaxBid(List<AuctionBid> auctionBids) {
        AuctionBid maxBid = new AuctionBid();
        maxBid.setBidAmount(BigDecimal.ZERO);
        for (AuctionBid bid : auctionBids) {
            if (bid.getBidAmount().compareTo(maxBid.getBidAmount()) > 0) {
                maxBid = bid;
            }
        }
        return maxBid;
    }
}
