//
//  BottomSheetView.swift
//  MeetMap_ios
//
//  Created by Ilya Prokofev on 15.08.2024.
//

import SwiftUI

import SwiftUI

// Объявление структуры BottomSheetView, которая представляет собой пользовательский компонент нижнего окна (Bottom Sheet)
struct BottomSheetView<Content: View>: View {
    // Связь с состоянием, которое управляет тем, открыто ли нижнее окно
    @Binding var isOpen: Bool
    // Минимальная высота окна
    let minHeight: CGFloat
    // Максимальная высота окна
    let maxHeight: CGFloat
    // Контент, который будет отображаться внутри окна
    let content: Content

    // Индикатор, который показывает, что окно можно перетаскивать
    private var indicator: some View {
        RoundedRectangle(cornerRadius: 8) // Закругленный прямоугольник
            .fill(Color.gray.opacity(0.5)) // Цвет с прозрачностью
            .frame(width: 50, height: 8) // Размеры индикатора
            .padding(8) // Отступ вокруг индикатора
    }

    // Вычисляемое свойство для определения смещения окна по вертикали
    private var offset: CGFloat {
        let maxOffset = maxHeight - minHeight // Максимальное смещение
        return isOpen ? 0 : maxOffset // Если окно открыто, смещение 0; если закрыто, смещение равно максимальному
    }

    // Инициализатор для BottomSheetView
    init(isOpen: Binding<Bool>, minHeight: CGFloat = 100, maxHeight: CGFloat, @ViewBuilder content: () -> Content) {
        self._isOpen = isOpen // Инициализация привязки состояния
        self.minHeight = minHeight // Установка минимальной высоты
        self.maxHeight = maxHeight // Установка максимальной высоты
        self.content = content() // Установка контента, который будет отображаться
    }

    // Основное представление для BottomSheetView
    var body: some View {
        GeometryReader { geometry in
            VStack {
                Spacer() // Заполняет пространство сверху, чтобы окно было внизу экрана
                VStack {
                    self.indicator // Показывает индикатор перетаскивания
                    ScrollView { // Добавляем ScrollView для прокрутки содержимого
                        self.content // Отображаем переданный контент
                    }
                }
                .frame(width: geometry.size.width, height: maxHeight, alignment: .top) // Устанавливаем ширину и высоту окна
                .background(Color.white) // Фоновый цвет окна
                .cornerRadius(30) // Закругляем углы окна
                .offset(y: self.offset) // Применяем вертикальное смещение
                .padding(.bottom, 20) // Добавляем отступ снизу
                .gesture(
                    DragGesture() // Обрабатываем жест перетаскивания
                        .onChanged { value in
                            if value.translation.height > 0 { // Если жест идет вниз
                                self.isOpen = false // Закрыть окно
                            } else { // Если жест идет вверх
                                self.isOpen = true // Открыть окно
                            }
                        }
                )
            }
        }
        .animation(.interactiveSpring(), value: isOpen) // Добавляем анимацию при изменении состояния
    }
}
