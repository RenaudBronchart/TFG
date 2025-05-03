package com.example.tfg


import com.example.tfg.viewmodel.ProductViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class ProductViewModelTest {

    private lateinit var viewModel: ProductViewModel

    @Before
    fun setup() {
        viewModel = ProductViewModel(FakeProductRepository())
    }

    @Test
    fun testResetFields() = runBlocking {

        viewModel.onCompletedFields(
            name = "Produit test",
            price = 10.0,
            description = "Description test",
            category = "Catégorie test",
            image = "Image test",
            stock = 5,
            brand = "Marque test"
        )


        assertEquals("Produit test", viewModel.name.value)
        assertEquals(10.0, viewModel.price.value, 0.0)
        assertEquals("Description test", viewModel.description.value)
        assertEquals("Catégorie test", viewModel.category.value)
        assertEquals("Image test", viewModel.image.value)
        assertEquals(5, viewModel.stock.value)
        assertEquals("Marque test", viewModel.brand.value)


        viewModel.resetFields()


        assertEquals("", viewModel.name.value)
        assertEquals(0.0, viewModel.price.value, 0.0)
        assertEquals("", viewModel.description.value)
        assertEquals("Selecciona una categoría", viewModel.category.value)
        assertEquals("", viewModel.image.value)
        assertEquals(0, viewModel.stock.value)
        assertEquals("", viewModel.brand.value)
        assertEquals(false, viewModel.isButtonEnable.value)
    }

}
