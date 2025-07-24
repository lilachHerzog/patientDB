using System;
using System.Windows;
using System.Windows.Media;

namespace patientApp.Extensions
{
    public static class UIExtensions
    {
        public static T ParentOfType<T>(this DependencyObject element) where T : DependencyObject
        {
            if (element == null)
                return null;

            DependencyObject parent = VisualTreeHelper.GetParent(element);
            while (parent != null && !(parent is T))
            {
                parent = VisualTreeHelper.GetParent(parent);
            }

            return parent as T;
        }
    }
}
