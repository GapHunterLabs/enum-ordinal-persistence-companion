package dev.gaphunter.enumordinalpersistencecompanion.inspection

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class OrdinalPersistenceInspectionTest : BasePlatformTestCase() {

    override fun setUp() {
        super.setUp()
        myFixture.enableInspections(OrdinalPersistenceInspection::class.java)
    }

    fun `test Enumerated with no value argument on an Entity is flagged`() {
        myFixture.configureByText(
            "Order.java",
            """
            import javax.persistence.Entity;
            import javax.persistence.Enumerated;

            @Entity
            class Order {
                @Enumerated
                private OrderStatus status;
            }

            enum OrderStatus { PENDING, SHIPPED, DELIVERED }
            """.trimIndent(),
        )
        val highlights = myFixture.doHighlighting()
        assertTrue(highlights.any { it.description?.contains("ORDINAL") == true })
    }

    fun `test Enumerated with explicit EnumType ORDINAL on an Entity is flagged`() {
        myFixture.configureByText(
            "Order2.java",
            """
            import javax.persistence.Entity;
            import javax.persistence.Enumerated;
            import javax.persistence.EnumType;

            @Entity
            class Order2 {
                @Enumerated(EnumType.ORDINAL)
                private OrderStatus2 status;
            }

            enum OrderStatus2 { PENDING, SHIPPED, DELIVERED }
            """.trimIndent(),
        )
        val highlights = myFixture.doHighlighting()
        assertTrue(highlights.any { it.description?.contains("ORDINAL") == true })
    }

    fun `test Enumerated with explicit EnumType STRING is not flagged`() {
        myFixture.configureByText(
            "Order3.java",
            """
            import javax.persistence.Entity;
            import javax.persistence.Enumerated;
            import javax.persistence.EnumType;

            @Entity
            class Order3 {
                @Enumerated(EnumType.STRING)
                private OrderStatus3 status;
            }

            enum OrderStatus3 { PENDING, SHIPPED, DELIVERED }
            """.trimIndent(),
        )
        val highlights = myFixture.doHighlighting()
        assertTrue(highlights.none { it.description?.contains("ORDINAL") == true })
    }

    fun `test Enumerated on a class without Entity is never flagged`() {
        myFixture.configureByText(
            "PlainDto.java",
            """
            import javax.persistence.Enumerated;

            class PlainDto {
                @Enumerated
                private Status status;
            }

            enum Status { A, B }
            """.trimIndent(),
        )
        val highlights = myFixture.doHighlighting()
        assertTrue(highlights.none { it.description?.contains("ORDINAL") == true })
    }

    fun `test a field with no Enumerated annotation is never flagged`() {
        myFixture.configureByText(
            "Order4.java",
            """
            import javax.persistence.Entity;

            @Entity
            class Order4 {
                private String status;
            }
            """.trimIndent(),
        )
        val highlights = myFixture.doHighlighting()
        assertTrue(highlights.none { it.description?.contains("ORDINAL") == true })
    }
}
