package com.library.config;

import com.library.entity.Book;
import com.library.entity.BorrowRecord;
import com.library.entity.Member;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRecordRepository;
import com.library.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    @Autowired
    public DataSeeder(BookRepository bookRepository,
                      MemberRepository memberRepository,
                      BorrowRecordRepository borrowRecordRepository) {
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.borrowRecordRepository = borrowRecordRepository;
    }

    @Override
    public void run(String... args) {
        if (bookRepository.count() > 0) return; // Don't seed if data exists

        // ---- Seed Books ----
        List<Book> books = Arrays.asList(
            new Book("The Great Gatsby", "F. Scott Fitzgerald", "978-0743273565",
                     "Fiction", "Scribner", 1925, 5,
                     "A story of the fabulously wealthy Jay Gatsby and his love for Daisy Buchanan.", null),
            new Book("To Kill a Mockingbird", "Harper Lee", "978-0061935466",
                     "Fiction", "HarperCollins", 1960, 4,
                     "The story of racial injustice and the loss of innocence in the American South.", null),
            new Book("1984", "George Orwell", "978-0451524935",
                     "Dystopian", "Signet Classic", 1949, 6,
                     "A dystopian novel about totalitarianism and mass surveillance.", null),
            new Book("Pride and Prejudice", "Jane Austen", "978-0141439518",
                     "Romance", "Penguin Classics", 1813, 3,
                     "A romantic novel that charts the emotional development of protagonist Elizabeth Bennet.", null),
            new Book("The Alchemist", "Paulo Coelho", "978-0062315007",
                     "Philosophy", "HarperOne", 1988, 5,
                     "A philosophical novel about a young Andalusian shepherd's journey to the pyramids.", null),
            new Book("Harry Potter and the Sorcerer's Stone", "J.K. Rowling", "978-0590353427",
                     "Fantasy", "Scholastic", 1997, 8,
                     "The story of a young wizard's first year at Hogwarts School of Witchcraft and Wizardry.", null),
            new Book("The Da Vinci Code", "Dan Brown", "978-0307474278",
                     "Thriller", "Anchor", 2003, 4,
                     "A mystery-thriller novel involving a secret society and the Holy Grail.", null),
            new Book("Clean Code", "Robert C. Martin", "978-0132350884",
                     "Technology", "Prentice Hall", 2008, 3,
                     "A handbook of agile software craftsmanship.", null),
            new Book("Atomic Habits", "James Clear", "978-0735211292",
                     "Self-Help", "Avery", 2018, 5,
                     "An easy and proven way to build good habits and break bad ones.", null),
            new Book("Sapiens", "Yuval Noah Harari", "978-0062316097",
                     "History", "Harper", 2011, 4,
                     "A brief history of humankind.", null)
        );
        bookRepository.saveAll(books);

        // ---- Seed Members ----
        List<Member> members = Arrays.asList(
            new Member("Arjun", "Sharma", "arjun.sharma@email.com", "9876543210", "123 MG Road, Hyderabad"),
            new Member("Priya", "Patel", "priya.patel@email.com", "9876543211", "456 Banjara Hills, Hyderabad"),
            new Member("Rahul", "Kumar", "rahul.kumar@email.com", "9876543212", "789 Jubilee Hills, Hyderabad"),
            new Member("Sneha", "Reddy", "sneha.reddy@email.com", "9876543213", "321 Gachibowli, Hyderabad"),
            new Member("Vikram", "Singh", "vikram.singh@email.com", "9876543214", "654 Madhapur, Hyderabad")
        );
        memberRepository.saveAll(members);

        // ---- Seed Borrow Records ----
        Book book1 = books.get(0);
        Book book2 = books.get(2);
        Member member1 = members.get(0);
        Member member2 = members.get(1);

        // Active borrow
        BorrowRecord record1 = new BorrowRecord(book1, member1,
                LocalDate.now().minusDays(5), LocalDate.now().plusDays(9));
        borrowRecordRepository.save(record1);
        book1.setAvailableCopies(book1.getAvailableCopies() - 1);
        bookRepository.save(book1);

        // Overdue borrow
        BorrowRecord record2 = new BorrowRecord(book2, member2,
                LocalDate.now().minusDays(20), LocalDate.now().minusDays(6));
        record2.setStatus(BorrowRecord.BorrowStatus.OVERDUE);
        borrowRecordRepository.save(record2);
        book2.setAvailableCopies(book2.getAvailableCopies() - 1);
        bookRepository.save(book2);

        System.out.println("============================================");
        System.out.println("  Library System - Sample Data Loaded!");
        System.out.println("  Books:   " + bookRepository.count());
        System.out.println("  Members: " + memberRepository.count());
        System.out.println("  Borrows: " + borrowRecordRepository.count());
        System.out.println("  H2 Console: http://localhost:8080/h2-console");
        System.out.println("  API Base:   http://localhost:8080/api");
        System.out.println("============================================");
    }
}
