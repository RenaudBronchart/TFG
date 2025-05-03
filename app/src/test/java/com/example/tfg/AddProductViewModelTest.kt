package com.example.tfg

import com.example.tfg.models.Product
import com.example.tfg.repository.IProductRepository
import com.example.tfg.viewmodel.AddProductViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddProductViewModelTest {

    private lateinit var viewModel: AddProductViewModel
    private val testDispatcher = StandardTestDispatcher()

    class FakeProductRepository : IProductRepository {
        override suspend fun getProducts(): List<Product> = emptyList()
        override suspend fun getProductById(uid: String): Product? = null
        override suspend fun addProduct(product: Product): Result<String> = Result.success("Produit ajouté (fake)")
        override suspend fun updateProduct(productId: String, product: Product): Boolean = true
        override suspend fun deleteProductById(productId: String): Boolean = true
        override suspend fun uploadImageToStorage(imageUri: android.net.Uri): Result<String> = Result.success("url_fausse")
    }

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher) //
        viewModel = AddProductViewModel(FakeProductRepository())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testAddProductReturnsSuccess() = runTest {
        val product = Product(
            id = "123",
            name = "Produit test",
            price = 10.0,
            description = "Desc",
            category = "Catégorie",
            image = "url_image",
            stock = 5,
            brand = "Marque"
        )
        viewModel.addProduct(product) { message ->
            assertEquals("Producto  agregado (fake)", message)
            assertEquals("Producto agregado (fake)", viewModel.messageConfirmation.value)
        }
        testDispatcher.scheduler.advanceUntilIdle()
    }
}
